package com.young.xjyq.service;

import com.young.xjyq.common.PageInfo;
import com.young.xjyq.dto.FaceDto;
import com.young.xjyq.dto.OcrDto;
import com.young.xjyq.dto.VideoDto;
import com.young.xjyq.dto.VideoListDto;
import com.young.xjyq.vo.VideoSearchVo;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author error-codes【BayMax】
 * @see <a href="www.error-codes.xyz">BayMax Blog</a>
 * @since 2022/9/9 16:14
 */
public interface VideoService {

    void createVideo();

    void updateVideo();

    VideoDto readVideoById(Long id);

    PageInfo<VideoListDto> readAllVideo(Integer page, Integer pageSize, VideoSearchVo videoSearchVo);

    int batchDeleteVideo(List<Long> ids);

    FaceDto readFaceResult(String task);

    OcrDto readOcrResult(String task);

    int reanalyse(Long id);
}
