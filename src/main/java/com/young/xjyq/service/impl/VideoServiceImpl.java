package com.young.xjyq.service.impl;

import com.dtflys.forest.http.ForestResponse;
import com.young.xjyq.common.*;
import com.young.xjyq.dto.*;
import com.young.xjyq.dto.apiResult.*;
import com.young.xjyq.entity.Person;
import com.young.xjyq.entity.Tag;
import com.young.xjyq.entity.Video;
import com.young.xjyq.mapper.PersonMapper;
import com.young.xjyq.mapper.TagMapper;
import com.young.xjyq.mapper.VideoMapper;
import com.young.xjyq.service.RemoteVideoService;
import com.young.xjyq.service.VideoService;
import com.young.xjyq.util.SnowFlakeUtils;
import com.young.xjyq.util.YoungUtils;
import com.young.xjyq.vo.VideoSearchVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.support.ExecutorServiceAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author error-codes【BayMax】
 * @see <a href="www.error-codes.xyz">BayMax Blog</a>
 * @since 2022/9/9 16:14
 */
@Slf4j
@Service
public class VideoServiceImpl implements VideoService {

    private final VideoMapper videoMapper;
    private final PersonMapper personMapper;
    private final TagMapper tagMapper;
    private final RemoteVideoService remoteVideoService;

    private ExecutorService threadPool = new ThreadPoolExecutor(
            3,
            10,
            10,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(5),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.DiscardOldestPolicy());

    @Autowired
    public VideoServiceImpl(PersonMapper personMapper,
                            VideoMapper videoMapper,
                            TagMapper tagMapper, RemoteVideoService remoteVideoService) {
        this.personMapper = personMapper;
        this.videoMapper = videoMapper;
        this.tagMapper = tagMapper;
        this.remoteVideoService = remoteVideoService;
    }

    @Override
    public void createVideo() {
        int page = 1;
        List<String> videos = videoMapper.readAllVideo(null, null, null, null, null).stream().map(Video::getUrl).collect(Collectors.toList());
        while (true) {
            ResultPage<VideoGatherDto> videoGatherDtoResultPage = remoteVideoService.insertVideo(page++, 100, 1, 1, "bj.com", 1, "天脉电视", null);
            List<Video> videoList = videoGatherDtoResultPage.getData().stream().map(videoGatherDto -> {
                if (!videos.contains(videoGatherDto.getUrl())) {
                    ForestResponse<Result<CreateTaskDto>> faceTask = remoteVideoService.createFaceTask(videoGatherDto.getUrl());
                    ForestResponse<Result<CreateTaskDto>> ocrTask = remoteVideoService.createOcrTask(videoGatherDto.getUrl());

                    long snowFlakeId = SnowFlakeUtils.getSnowFlakeId();
                    if (faceTask.isTimeout() || faceTask.isError()) {
                        log.warn("创建视频[视频ID: {}, 视频URL: {}]人像识别任务失败", snowFlakeId, videoGatherDto.getUrl());
                    }
                    if (ocrTask.isTimeout() || ocrTask.isError()) {
                        log.warn("创建视频[视频ID: {}, 视频URL: {}]文本识别任务失败", snowFlakeId, videoGatherDto.getUrl());
                    }
                    return new Video(snowFlakeId,
                            new Date(),
                            new Date(),
                            null,
                            videoGatherDto.getUrl(),
                            videoGatherDto.getSiteName(),
                            "-1&",
                            0,
                            faceTask.isSuccess() ? faceTask.getResult().getData().getId() : null,
                            ocrTask.isSuccess() ? ocrTask.getResult().getData().getId() : null,
                            -1,
                            -1);
                }
                return null;
            }).collect(Collectors.toList()).stream().filter(Objects::nonNull).collect(Collectors.toList());

            if (!videoList.isEmpty()) {
                videoMapper.createVideo(videoList);
            }

            if (videoGatherDtoResultPage.getTotalCount() == 0) {
                break;
            }
        }
    }

    @Override
    public void updateVideo() {
        List<Video> videoList = videoMapper.readAllVideo(null, null, null, null, null);
        final CountDownLatch latch = new CountDownLatch(3);
        videoList.forEach(video -> {
            threadPool.execute(() -> {
                if (video.getFaceStatus() != 1) {
                    Result<FaceTaskResultDto> faceTaskResultDtoResult = remoteVideoService.readTaskFaceStatus(video.getFaceTaskId());
                    int faceTaskStatus = Integer.parseInt(faceTaskResultDtoResult.getData().getTask_status());
                    if (faceTaskStatus == 1) {
                        ResultList<FaceMatchDto> faceMatchDtoResultList = remoteVideoService.readTaskFace(video.getFaceTaskId());
                        List<Person> personList = personMapper.readAllPerson();
                        List<Integer> ids = YoungUtils.getTag(video.getTags());
                        for (FaceMatchDto matchDto : faceMatchDtoResultList.getData()) {
                            for (Person person : personList) {
                                if(matchDto.getPerson_id().equals(person.getId())) {
                                    video.setTags(YoungUtils.mergeTagList(YoungUtils.contains(ids, person.getTags())));
                                }
                            }
                        }
                    }
                    video.setFaceStatus(faceTaskStatus);
                    latch.countDown();
                }
            });

            threadPool.execute(() -> {
                if (video.getOcrStatus() != 1) {
                    Result<OcrTaskResultDto> ocrTaskResultDtoResult = remoteVideoService.readTaskOcrStatus(video.getOcrTaskId());
                    int ocrTaskStatus = Integer.parseInt(ocrTaskResultDtoResult.getData().getTask_status());
                    video.setOcrStatus(ocrTaskStatus);
                }
            });

            threadPool.execute(() -> {
                if (video.getOcrStatus() != 1 || YoungUtils.getTag(video.getStatus()).contains(-1)) {
                    OcrDto ocrDto = readOcrResult(video.getOcrTaskId());
                    StringBuilder stringBuilder = new StringBuilder();
                    ocrDto.getOcrDtos().forEach(ocr -> stringBuilder.append(ocr.getText()));
                    List<Integer> status = new ArrayList<>();
                    if (remoteVideoService.pornDetection(stringBuilder.toString()).getPredictions().get(0).getPredictions().get("label").equals("1")) {
                        status.add(1);
                        video.setStatus(YoungUtils.mergeTagList(status));
                    } else if (remoteVideoService.violationDetection(stringBuilder.toString()).getPredictions().get(0).getPredictions().get("label").equals("1")) {
                        status.add(2);
                        video.setStatus(YoungUtils.mergeTagList(status));
                    } else if (remoteVideoService.adDetection(stringBuilder.toString()).getPredictions().get(0).getPredictions().get("label").equals("1")) {
                        status.add(3);
                        video.setStatus(YoungUtils.mergeTagList(status));
                    } else {
                        video.setStatus(YoungUtils.mergeTagList(Collections.singletonList(0)));
                    }
                    latch.countDown();
                }
            });

            try {
                latch.await();
                videoMapper.updateVideoById(video.getId(), video.getStatus(), null, null, video.getFaceStatus(), video.getOcrStatus());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public VideoDto readVideoById(Long id) {
        Video video = videoMapper.readVideoById(id);

        if (Objects.isNull(video)) {
            return null;
        }
        return new VideoDto(video.getId(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(video.getCreateTime()),
                tagMapper.readTagByIds(YoungUtils.getTag(video.getTags())),
                video.getUrl(),
                video.getSource(),
                YoungUtils.getTag(video.getStatus()),
                video.getFaceTaskId(),
                video.getOcrTaskId(),
                video.getFaceStatus(),
                video.getOcrStatus());
    }

    @Override
    @Transactional
    public PageInfo<VideoListDto> readAllVideo(Integer page, Integer pageSize, VideoSearchVo videoSearchVo) {
        List<Video> videoList = videoMapper.readAllVideo((page - 1) * pageSize, pageSize, videoSearchVo.getStartTime(), videoSearchVo.getEndTime(), videoSearchVo.getAuthor());
        List<VideoListDto> videoListDtos = videoList.parallelStream().map(video -> {
            List<Tag> tags = tagMapper.readTagByIds(YoungUtils.getTag(video.getTags()));
            return new VideoListDto(video.getId(),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(video.getCreateTime()),
                    tags,
                    video.getUrl(),
                    video.getSource(),
                    YoungUtils.getTag(video.getStatus()),
                    video.getFaceTaskId(),
                    video.getOcrTaskId(),
                    video.getFaceStatus(),
                    video.getOcrStatus());
        }).collect(Collectors.toList());
        return new PageInfo<>(page, pageSize, videoMapper.countVideo(videoSearchVo.getStartTime(), videoSearchVo.getEndTime(), videoSearchVo.getAuthor()), videoListDtos);
    }

    @Override
    public int batchDeleteVideo(List<Long> ids) {
        return videoMapper.batchDeleteVideo(ids);
    }

    @Override
    public FaceDto readFaceResult(String task) {
        ResultList<FaceMatchDto> faceMatchDtoResult = remoteVideoService.readTaskFace(task);

        List<PersonDto> personDtos = faceMatchDtoResult.getData().parallelStream().map(faceMatchDto -> {
            Integer personId = faceMatchDto.getPerson_id();
            Person person = personMapper.readPersonById(faceMatchDto.getPerson_id());
            return new PersonDto(personId,
                    person == null ? "未入库" : faceMatchDto.getPerson_name(),
                    faceMatchDto.getFace_url(),
                    person == null ? null : tagMapper.readTagByIds(YoungUtils.getTag(person.getTags())),
                    person == null ? null : person.getRemark());
        }).collect(Collectors.toList());

        return new FaceDto(personDtos);
    }

    @Override
    public OcrDto readOcrResult(String task) {
        ResultList<OcrMatchDto> ocrMatchDtoResult = remoteVideoService.readTaskOcr(task);
        List<Ocr> ocrs = ocrMatchDtoResult.getData().parallelStream().map(ocrMatchDto ->
                new Ocr(ocrMatchDto.getIntime(), ocrMatchDto.getOcr_result())
        ).collect(Collectors.toList());
        return new OcrDto(ocrs);
    }

    @Override
    public int reanalyse(Long id) {
        Video video = videoMapper.readVideoById(id);

        final CountDownLatch latch = new CountDownLatch(2);
        threadPool.execute(() -> {
            ForestResponse<Result<CreateTaskDto>> faceTask = remoteVideoService.createFaceTask(video.getUrl());
            if (!faceTask.isTimeout()) {
                video.setFaceTaskId(faceTask.getResult().getData().getId());
                video.setFaceStatus(-1);
            } else {
                log.warn("重建视频[视频ID: {}, 视频URL: {}]人像识别任务失败, 网络超时!", video.getId(), video.getUrl());
            }
            latch.countDown();
        });

        threadPool.execute(() -> {
            ForestResponse<Result<CreateTaskDto>> ocrTask = remoteVideoService.createOcrTask(video.getUrl());
            if (!ocrTask.isTimeout()) {
                video.setOcrTaskId(ocrTask.getResult().getData().getId());
                video.setOcrStatus(-1);
            } else {
                log.warn("重建视频[视频ID: {}, 视频URL: {}]文本识别任务失败, 网络超时!", video.getId(), video.getUrl());
            }
            video.setStatus(YoungUtils.mergeTagList(Collections.singletonList(-1)));
            latch.countDown();
        });


        return videoMapper.updateVideoById(id, video.getStatus(), video.getFaceTaskId(), video.getOcrTaskId(), -1, -1);
    }
}
