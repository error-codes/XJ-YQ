package com.young.xjyq.service.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author error-codes【BayMax】
 * @see <a href="www.error-codes.xyz">BayMax Blog</a>
 * @since 2022/9/9 16:14
 */
@Service
public class VideoServiceImpl implements VideoService {

    private final VideoMapper videoMapper;
    private final PersonMapper personMapper;
    private final TagMapper tagMapper;
    private final RemoteVideoService remoteVideoService;

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
            ResultPage<VideoGatherDto> videoGatherDtoResultPage = remoteVideoService.insertVideo(page++, 10, 1, "bj.com", 1, "天脉电视", null);
            List<Video> videoList = videoGatherDtoResultPage.getData().stream().map(videoGatherDto -> {
                if (!videos.contains(videoGatherDto.getUrl())) {
                    Result<CreateTaskDto> faceTask = remoteVideoService.createFaceTask(videoGatherDto.getUrl());
                    Result<CreateTaskDto> orcTask = remoteVideoService.createOcrTask(videoGatherDto.getUrl());
                    return new Video(SnowFlakeUtils.getSnowFlakeId(),
                            new Date(),
                            new Date(),
                            null,
                            videoGatherDto.getUrl(),
                            videoGatherDto.getSiteName(),
                            0,
                            0,
                            faceTask.getData().getId(),
                            orcTask.getData().getId(),
                            0,
                            0);
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
        videoList.forEach(video -> {
            Result<FaceTaskResultDto> faceTaskResultDtoResult = remoteVideoService.readTaskFaceStatus(video.getFaceTaskId());
            Result<OcrTaskResultDto> ocrTaskResultDtoResult = remoteVideoService.readTaskOcrStatus(video.getOcrTaskId());
            int faceTaskStatus = Integer.parseInt(faceTaskResultDtoResult.getData().getTask_status());
            int ocrTaskStatus = Integer.parseInt(ocrTaskResultDtoResult.getData().getTask_status());
            if (video.getFaceStatus() != faceTaskStatus) {
                video.setFaceStatus(faceTaskStatus);
            }
            if (video.getOcrStatus() != ocrTaskStatus) {
                video.setOcrStatus(ocrTaskStatus);
            }
            OcrDto ocrDto = readOcrResult(video.getOcrTaskId());
            StringBuilder stringBuilder = new StringBuilder();
            ocrDto.getOcrDtos().forEach(ocr -> stringBuilder.append(ocr.getText()));
            if (remoteVideoService.pornDetection(stringBuilder.toString()).getPredictions().get(0).getPredictions().get("label").equals("1")) {
                video.setStatus(1);
            }
            if (remoteVideoService.violationDetection(stringBuilder.toString()).getPredictions().get(0).getPredictions().get("label").equals("1")) {
                video.setStatus(1);
            }
            if (remoteVideoService.adDetection(stringBuilder.toString()).getPredictions().get(0).getPredictions().get("label").equals("1")) {
                video.setStatus(1);
            }
            videoMapper.updateVideoById(video.getId(), 0, null, null, video.getFaceStatus(), video.getOcrStatus());
        });
    }

    @Override
    public VideoDto readVideoById(Long id) {
        Video video = videoMapper.readVideoById(id);

        if (Objects.isNull(video)) {
            return null;
        }
        return new VideoDto(video.getId(),
                video.getCreateTime(),
                tagMapper.readTagByIds(YoungUtils.getTag(video.getTags())),
                video.getUrl());
    }

    @Override
    @Transactional
    public PageInfo<VideoListDto> readAllVideo(Integer page, Integer pageSize, VideoSearchVo videoSearchVo) {
        List<Video> videoList = videoMapper.readAllVideo((page - 1) * pageSize, pageSize, videoSearchVo.getStartTime(), videoSearchVo.getEndTime(), videoSearchVo.getAuthor());
        List<VideoListDto> videoListDtos = videoList.stream().map(video -> {
            List<Tag> tags = tagMapper.readTagByIds(YoungUtils.getTag(video.getTags()));
            return new VideoListDto(video.getId(),
                    video.getCreateTime(),
                    tags,
                    video.getUrl(),
                    video.getSource(),
                    video.getStatus(),
                    video.getFaceStatus(),
                    video.getOcrStatus());
        }).collect(Collectors.toList());
        return new PageInfo<>(page, pageSize, videoMapper.countVideo(), videoListDtos);
    }

    @Override
    public int batchDeleteVideo(List<Long> ids) {
        return videoMapper.batchDeleteVideo(ids);
    }

    @Override
    public FaceDto readFaceResult(String task) {
        ResultList<FaceMatchDto> faceMatchDtoResult = remoteVideoService.readTaskFace(task);

        List<Integer> tags = new ArrayList<>();
        List<PersonDto> personDtos = faceMatchDtoResult.getData().stream().map(faceMatchDto -> {
            Integer personId = faceMatchDto.getPerson_id();
            Person person = personMapper.readPersonById(faceMatchDto.getPerson_id());
            YoungUtils.contains(tags, person.getTags());
            return new PersonDto(personId,
                    faceMatchDto.getPerson_name(),
                    faceMatchDto.getFace_url(),
                    tagMapper.readTagByIds(YoungUtils.getTag(person.getTags())),
                    person.getRemark());
        }).collect(Collectors.toList());

        return new FaceDto(personDtos);
    }

    @Override
    public OcrDto readOcrResult(String task) {
        ResultList<OcrMatchDto> ocrMatchDtoResult = remoteVideoService.readTaskOcr(task);
        List<OcrDto.Ocr> ocrs = ocrMatchDtoResult.getData().stream().map(ocrMatchDto ->
                new OcrDto.Ocr(ocrMatchDto.getIntime(), ocrMatchDto.getOcr_result())
        ).collect(Collectors.toList());
        return new OcrDto(ocrs);
    }

    @Override
    public int reanalyse(Long id) {
        Video video = videoMapper.readVideoById(id);

        Result<CreateTaskDto> faceTask = remoteVideoService.createFaceTask(video.getUrl());
        Result<CreateTaskDto> ocrTask = remoteVideoService.createOcrTask(video.getUrl());

        return videoMapper.updateVideoById(id, 0, Integer.parseInt(faceTask.getData().getId()), Integer.parseInt(ocrTask.getData().getId()), 0, 0);
    }
}
