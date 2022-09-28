package com.young.xjyq.mapper;

import com.young.xjyq.entity.Video;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/11 21:13
 */
@Mapper
public interface VideoMapper {

    Video readVideoById(Long id);

    Video readVideoByUrl(String url);

    Integer countFinallyVideo(Date startTime, Date endTime, String author);

    List<Video> readAllVideo(Integer page, Integer pageSize, Date startTime, Date endTime, String author);

    List<Video> readFinallyVideo(Integer page, Integer pageSize, Date startTime, Date endTime, String author);

    int updateVideoById(Long id, Integer status, String tags, String faceTaskId, String ocrTaskId, Integer faceStatus, Integer ocrStatus, String illegal);

    int batchDeleteVideo(List<Long> ids);

    int createVideo(List<Video> videos);
}
