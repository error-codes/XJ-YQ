package com.young.xjyq.service;

import com.dtflys.forest.annotation.*;
import com.dtflys.forest.http.ForestResponse;
import com.young.xjyq.common.*;
import com.young.xjyq.dto.apiResult.*;
import com.young.xjyq.util.YoungUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/11 20:56
 */
@Service
public interface RemoteVideoService {

    String xinruiApi = "http://116.178.87.241:9898";

    String checkApi = "http://47.95.50.249:7012";

    String videoApi = "http://101.200.125.197";
    @Get(url = videoApi + "/es_push/servlet/DataPushServlet/")
    ResultPage<VideoGatherDto> insertVideo(@Query("pageNo") Integer page,
                                           @Query("pageSize") Integer pageSize,
                                           @Query("time") Integer time,
                                           @Query("timeType") Integer timeType,
                                           @Query("domain2") String domain,
                                           @Query("type") Integer type,
                                           @Query("author") String author,
                                           @Query("keywords") String keyword);

    @Post(url = xinruiApi + "/structure/v1/upload_video/")
    ForestResponse<Result<CreateTaskDto>> createFaceTask(@Body("video_url") String videoUrl);

    @Get(url = xinruiApi + "/structure/v1/{taskId}/taskstatus/")
    Result<FaceTaskResultDto> readTaskFaceStatus(@Query("taskId") String taskId);

    @Get(url = xinruiApi + "/structure/v1/{taskId}/faces/")
    ResultList<FaceMatchDto> readTaskFace(@Query("taskId") String taskId);

    @Post(url = xinruiApi + "/structure/v3/ocr/upload_video/")
    ForestResponse<Result<CreateTaskDto>> createOcrTask(@Body("video_url") String videoUrl);

    @Get(url = xinruiApi + "/structure/v3/ocr/{taskId}/taskstatus/")
    Result<OcrTaskResultDto> readTaskOcrStatus(@Query("taskId") String taskId);

    @Get(url = xinruiApi + "/structure/v3/ocr/{taskId}/result/")
    ResultCount<OcrMatchDto> readTaskOcr(@Query("taskId") String taskId,
                                         @Query("page") Integer page,
                                         @Query("limit") Integer limit);

    @Post(url = checkApi + "/v1/api/61519faffee774527a19ea9e?token=363326947")
    WiseResult pornDetection(@JSONBody("content") String text);

    @Post(url = checkApi + "/v1/api/6183ade6b85b6da0f265e169?token=363326947")
    WiseResult violationDetection(@JSONBody("content") String text);

    @Post(url = checkApi + "/v1/api/6232e395a0dd49a3d4394c67?token=363326947")
    WiseResult adDetection(@JSONBody("content") String text);

}
