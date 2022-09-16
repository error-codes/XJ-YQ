package com.young.xjyq.service;

import com.dtflys.forest.annotation.*;
import com.dtflys.forest.http.ForestResponse;
import com.young.xjyq.common.Result;
import com.young.xjyq.common.ResultList;
import com.young.xjyq.common.ResultPage;
import com.young.xjyq.common.WiseResult;
import com.young.xjyq.dto.apiResult.*;
import org.springframework.stereotype.Service;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/11 20:56
 */
@Service
public interface RemoteVideoService {

    @Get(url = "http://101.200.125.197/es_push/servlet/DataPushServlet/")
    ResultPage<VideoGatherDto> insertVideo(@Query("pageNo") Integer page,
                                           @Query("pageSize") Integer pageSize,
                                           @Query("time") Integer time,
                                           @Query("timeType") Integer timeType,
                                           @Query("domain2") String domain,
                                           @Query("type") Integer type,
                                           @Query("author") String author,
                                           @Query("keyword") String keyword);

    @Post(url = "http://192.17.1.20:9898/structure/v1/upload_video/")
    ForestResponse<Result<CreateTaskDto>> createFaceTask(@Body("video_url") String videoUrl);

    @Get(url = "http://192.17.1.20:9898/structure/v1/{taskId}/taskstatus/")
    Result<FaceTaskResultDto> readTaskFaceStatus(@Query("taskId") String taskId);

    @Get(url = "http://192.17.1.20:9898/structure/v1/{taskId}/faces/")
    ResultList<FaceMatchDto> readTaskFace(@Query("taskId") String taskId);

    @Post(url = "http://192.17.1.20:9898/structure/v3/ocr/upload_video/")
    ForestResponse<Result<CreateTaskDto>> createOcrTask(@Body("video_url") String videoUrl);

    @Get(url = "http://192.17.1.20:9898/structure/v3/ocr/{taskId}/taskstatus/")
    Result<OcrTaskResultDto> readTaskOcrStatus(@Query("taskId") String taskId);

    @Get(url = "http://192.17.1.20:9898/structure/v3/ocr/{taskId}/result/")
    ResultList<OcrMatchDto> readTaskOcr(@Query("taskId") String taskId);

    @Post(url = "http://122.14.231.158:6002/v1/api/61519faffee774527a19ea9e?token=363326947")
    WiseResult pornDetection(@JSONBody("content") String text);

    @Post(url = "http://122.14.231.158:6002/v1/api/614dad2fad36e89f8f734f61?token=363326947")
    WiseResult violationDetection(@JSONBody("content") String text);

    @Post(url = "http://122.14.231.158:6002/v1/api/60e2d49da0e3c1b19799051f?token=363326947")
    WiseResult adDetection(@JSONBody("content") String text);

}
