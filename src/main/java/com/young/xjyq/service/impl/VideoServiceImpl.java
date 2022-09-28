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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    public VideoServiceImpl(PersonMapper personMapper,
                            VideoMapper videoMapper,
                            TagMapper tagMapper,
                            RemoteVideoService remoteVideoService) {
        this.personMapper = personMapper;
        this.videoMapper = videoMapper;
        this.tagMapper = tagMapper;
        this.remoteVideoService = remoteVideoService;
    }

    @Override
    public void createVideo() {
        int page = 1;
        while (true) {
            List<String> videos = videoMapper.readAllVideo(null, null, null, null, null).stream().map(Video::getUrl).collect(Collectors.toList());
            ResultPage<VideoGatherDto> videoGatherDtoResultPage = remoteVideoService.insertVideo(page++, 40, 25, 1, null, 1, "天脉电视", "新疆");
            List<Video> videoList = videoGatherDtoResultPage.getData().stream().map(videoGatherDto -> {
                videoGatherDto.setUrl(videoGatherDto.getUrl().replace("http://www.zyhz-bj.com:18081", "http://8.142.158.96:18081")
                        .replace("http://www.zyhz-bj.com:22223", "http://8.142.158.96:18081"));
                if (!videos.contains(videoGatherDto.getUrl())) {
                    ForestResponse<Result<CreateTaskDto>> faceTask = remoteVideoService.createFaceTask(videoGatherDto.getUrl());
                    ForestResponse<Result<CreateTaskDto>> ocrTask = remoteVideoService.createOcrTask(videoGatherDto.getUrl());

                    long snowFlakeId = SnowFlakeUtils.getSnowFlakeId();
                    if (faceTask.isTimeout() || faceTask.isError()) {
                        log.warn("创建视频人像识别任务失败, [视频ID: {}, 视频URL: {}]", snowFlakeId, videoGatherDto.getUrl());
                    }
                    if (ocrTask.isTimeout() || ocrTask.isError()) {
                        log.warn("创建视频文本识别任务失败, [视频ID: {}, 视频URL: {}]", snowFlakeId, videoGatherDto.getUrl());
                    }
                    return new Video(snowFlakeId,
                            new Date(),
                            new Date(),
                            null,
                            videoGatherDto.getUrl(),
                            videoGatherDto.getSiteName(),
                            -1,
                            0,
                            faceTask.isSuccess() ? faceTask.getResult().getData().getId() : null,
                            ocrTask.isSuccess() ? ocrTask.getResult().getData().getId() : null,
                            -1,
                            -1,
                            videoGatherDto.getSite_url(),
                            videoGatherDto.getSummary(),
                            "-1&");
                }
                return null;
            }).collect(Collectors.toList()).stream().filter(Objects::nonNull).collect(Collectors.toList());

            if (!videoList.isEmpty()) {
                videoMapper.createVideo(videoList);
            }

            if (videoGatherDtoResultPage.getData().isEmpty()) {
                break;
            }
        }
    }

    @Override
    public void updateVideo() {
        List<Video> videoList = videoMapper.readAllVideo(null, null, null, null, null);
        for (Video video : videoList) {
            if (video.getFaceStatus() != 1 && StringUtils.isNotBlank(video.getFaceTaskId())) {
                Result<FaceTaskResultDto> faceTaskResultDtoResult = remoteVideoService.readTaskFaceStatus(video.getFaceTaskId());
                int faceTaskStatus = Integer.parseInt(faceTaskResultDtoResult.getData().getTask_status());
                if (faceTaskStatus == 1) {
                    ResultList<FaceMatchDto> faceMatchDtoResultList = remoteVideoService.readTaskFace(video.getFaceTaskId());
                    List<Person> personList = personMapper.readAllPerson();
                    List<Integer> ids = YoungUtils.getTag(video.getTags());
                    for (FaceMatchDto matchDto : faceMatchDtoResultList.getData()) {
                        for (Person person : personList) {
                            if (matchDto.getPerson_id().equals(person.getId())) {
                                video.setTags(YoungUtils.mergeTagList(YoungUtils.contains(ids, person.getTags())));
                            }
                        }
                    }
                    video.setStatus(1);
                }
                video.setFaceStatus(faceTaskStatus);
            }


            if (video.getOcrStatus() != 1 && StringUtils.isNotBlank(video.getOcrTaskId())) {
                Result<OcrTaskResultDto> ocrTaskResultDtoResult = remoteVideoService.readTaskOcrStatus(video.getOcrTaskId());
                int ocrTaskStatus = Integer.parseInt(ocrTaskResultDtoResult.getData().getTask_status());
                if (ocrTaskStatus == 1) {
                    OcrDto ocrDto = readOcrResult(video.getOcrTaskId());
                    StringBuilder stringBuilder = new StringBuilder();
                    ocrDto.getOcrDtos().forEach(ocr -> stringBuilder.append(ocr.getText()).append("|"));
                    StringBuilder sb = new StringBuilder();

                    if (StringUtils.isNotBlank(stringBuilder.toString())) {
                        if ("1".equals(remoteVideoService.pornDetection(stringBuilder.toString()).getData().get(0).getLabel())) {
                            sb.append("10&");
                            video.setTags((StringUtils.isBlank(video.getTags()) ? "" : video.getTags()) + "10&");
                        }
                        if ("1".equals(remoteVideoService.violationDetection(stringBuilder.toString()).getData().get(0).getLabel())) {
                            sb.append("11&");
                            video.setTags((StringUtils.isBlank(video.getTags()) ? "" : video.getTags()) + "11&");
                        }
                        if ("1".equals(remoteVideoService.adDetection(stringBuilder.toString()).getData().get(0).getLabel())) {
                            sb.append("12&");
                            video.setTags((StringUtils.isBlank(video.getTags()) ? "" : video.getTags()) + "12&");
                        }
                        if (StringUtils.isBlank(sb.toString())) {
                            sb.append("0&");
                        }
                    } else {
                        sb.append("0&");
                    }
                    video.setIllegal(sb.toString());
                }
                video.setOcrStatus(ocrTaskStatus);

            }

            videoMapper.updateVideoById(video.getId(), video.getStatus(), video.getTags(), null, null, video.getFaceStatus(), video.getOcrStatus(), video.getIllegal());
        }
    }

    @Override
    public VideoDto readVideoById(Long id) {
        Video video = videoMapper.readVideoById(id);

        if (Objects.isNull(video)) {
            return null;
        }

        return new VideoDto(video.getId(),
                video.getCoverImg(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(video.getCreateTime()),
                tagMapper.readTagByIds(YoungUtils.getTag(video.getTags())),
                video.getUrl(),
                video.getSiteUrl(),
                video.getSource(),
                video.getStatus(),
                video.getFaceTaskId(),
                video.getOcrTaskId(),
                video.getFaceStatus(),
                video.getOcrStatus(),
                tagMapper.readTagByIds(YoungUtils.getAllTag(video.getIllegal())));
    }

    @Override
    @Transactional
    public PageInfo<VideoListDto> readAllVideo(Integer page, Integer pageSize, VideoSearchVo videoSearchVo) {
        List<Video> videoList = videoMapper.readFinallyVideo((page - 1) * pageSize, pageSize, videoSearchVo.getStartTime(), videoSearchVo.getEndTime(), videoSearchVo.getAuthor());
        List<VideoListDto> videoListDtos = videoList.parallelStream().map(video -> {
            List<Tag> tags = tagMapper.readTagByIds(YoungUtils.getTag(video.getTags()));

            return new VideoListDto(video.getId(),
                    video.getCoverImg(),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(video.getCreateTime()),
                    tags,
                    video.getUrl(),
                    video.getSiteUrl(),
                    video.getSource(),
                    video.getStatus(),
                    video.getFaceTaskId(),
                    video.getOcrTaskId(),
                    video.getFaceStatus(),
                    video.getOcrStatus(),
                    tagMapper.readTagByIds(YoungUtils.getAllTag(video.getIllegal())));
        }).collect(Collectors.toList());
        return new PageInfo<>(page, pageSize, videoMapper.countFinallyVideo(videoSearchVo.getStartTime(), videoSearchVo.getEndTime(), videoSearchVo.getAuthor()), videoListDtos);
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
                    RemoteVideoService.xinruiApi + faceMatchDto.getFace_url(),
                    person == null ? null : tagMapper.readTagByIds(YoungUtils.getTag(person.getTags())),
                    person == null ? null : person.getRemark());
        }).collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                new TreeSet<>(Comparator.comparing(PersonDto::getId))), ArrayList::new));

        return new FaceDto(personDtos);
    }

    @Override
    public OcrDto readOcrResult(String task) {
        List<OcrMatchDto> ocrMatchDtos;
        ResultCount<OcrMatchDto> ocrMatchDtoResult = remoteVideoService.readTaskOcr(task, null, 100);

        if (ocrMatchDtoResult.getCount() <= 100) {
            ocrMatchDtos = ocrMatchDtoResult.getData();
        } else {
            ocrMatchDtos = remoteVideoService.readTaskOcr(task, null, ocrMatchDtoResult.getCount()).getData();
        }

        List<Ocr> ocrs = ocrMatchDtos.parallelStream().sorted(Comparator.comparing(OcrMatchDto::getIntime))
                .map(ocrMatchDto ->
                        new Ocr(ocrMatchDto.getIntime(), ocrMatchDto.getOcr_result())
                ).collect(Collectors.toList());
        return new OcrDto(ocrs);
    }

    @Override
    public int reanalyse(Long id) {
        Video video = videoMapper.readVideoById(id);


        ForestResponse<Result<CreateTaskDto>> faceTask = remoteVideoService.createFaceTask(video.getUrl());
        if (!faceTask.isTimeout()) {
            video.setFaceTaskId(faceTask.getResult().getData().getId());
            video.setFaceStatus(-1);
            video.setStatus(-1);
        } else {
            log.warn("重建视频[视频ID: {}, 视频URL: {}]人像识别任务失败, 网络超时!", video.getId(), video.getUrl());
        }


        ForestResponse<Result<CreateTaskDto>> ocrTask = remoteVideoService.createOcrTask(video.getUrl());
        if (!ocrTask.isTimeout()) {
            video.setOcrTaskId(ocrTask.getResult().getData().getId());
            video.setOcrStatus(-1);
        } else {
            log.warn("重建视频[视频ID: {}, 视频URL: {}]文本识别任务失败, 网络超时!", video.getId(), video.getUrl());
        }

        if (video.getOcrStatus() == -1) {
            video.setIllegal("-1&");
        }

        return videoMapper.updateVideoById(id, video.getStatus(), null, video.getFaceTaskId(), video.getOcrTaskId(), -1, -1, video.getIllegal());
    }
}
