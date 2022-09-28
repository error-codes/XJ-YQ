package com.young.xjyq.controller;

import com.young.xjyq.common.PageInfo;
import com.young.xjyq.common.Result;
import com.young.xjyq.dto.FaceDto;
import com.young.xjyq.dto.OcrDto;
import com.young.xjyq.dto.VideoDto;
import com.young.xjyq.dto.VideoListDto;
import com.young.xjyq.service.VideoService;
import com.young.xjyq.vo.VideoSearchVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author error-codes【BayMax】
 * @see <a href="www.error-codes.xyz">BayMax Blog</a>
 * @since 2022/9/9 16:07
 */
@Api(tags = "视频管理")
@RestController
@RequestMapping("/video")
public class VideoController {

    private final VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @Async
    @GetMapping("/1")
//    @Scheduled(cron = "0 0 0/2 * * *")
    public void insertVideo() {
        videoService.createVideo();
    }

    @Async
    @GetMapping("/2")
//    @Scheduled(cron = "0 0 0/2 * * *")
    public void updateVideo() {
        videoService.updateVideo();
    }

    @GetMapping("/readVideoById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "视频ID", dataType = "Long", dataTypeClass = Long.class)
    })
    @ApiOperation("根据视频ID获取视频详情")
    public Result<VideoDto> readVideoById(@RequestParam @NotNull Long id) {
        try {
            return new Result<>(200, "查询成功", videoService.readVideoById(id));
        } catch (Exception e) {
            return new Result<>(204, "查询失败", null);
        }
    }

    @GetMapping("/readAllVideo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "Integer", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value = "装载数量", dataType = "Integer", dataTypeClass = Integer.class),
    })
    @ApiOperation("获取视频列表")
    public Result<PageInfo<VideoListDto>> readAllVideo(@RequestParam(defaultValue = "1") Integer page,
                                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                                       VideoSearchVo videoSearchVo) {
        try {
            return new Result<>(200, "查询成功", videoService.readAllVideo(page, pageSize, videoSearchVo));
        } catch (Exception e) {
            return new Result<>(204, "查询失败", null);
        }
    }

    @PostMapping("/batchDeletePerson")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "视频ID列表", dataType = "Long", dataTypeClass = Long.class, allowMultiple = true)
    })
    @ApiOperation("批量删除视频")
    public Result<?> batchDeletePerson(@RequestParam @NotEmpty List<Long> ids) {
        int result = videoService.batchDeleteVideo(ids);
        if (result > 0) {
            return new Result<>(200, "删除成功", null);
        } else {
            return new Result<>(204, "删除失败", null);
        }
    }

    @PostMapping("/reanalyse")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "视频ID", dataType = "Long", dataTypeClass = Long.class)
    })
    @ApiOperation("重新分析")
    public Result<?> reanalyse(@RequestParam Long id) {
        int result = videoService.reanalyse(id);
        if (result > 0) {
            return new Result<>(200, "提交成功", null);
        } else {
            return new Result<>(204, "提交失败", null);
        }
    }

    @GetMapping("/readFaceResult")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "task", value = "人脸识别任务ID", dataType = "String", dataTypeClass = String.class)
    })
    @ApiOperation("获取人脸识别结果列表")
    public Result<FaceDto> readFaceResult(@RequestParam @NotBlank String task) {
        try {
            return new Result<>(200, "查询成功", videoService.readFaceResult(task));
        } catch (Exception e) {
            return new Result<>(204, "查询失败", null);
        }
    }

    @GetMapping("/readOcrResult")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "task", value = "文本识别任务ID", dataType = "String", dataTypeClass = String.class)
    })
    @ApiOperation("获取文本识别结果列表")
    public Result<OcrDto> readOcrResult(@RequestParam @NotBlank String task) {
        try {
            return new Result<>(200, "查询成功", videoService.readOcrResult(task));
        } catch (Exception e) {
            return new Result<>(204, "查询失败", null);
        }
    }
}
