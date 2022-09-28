package com.young.xjyq.dto;

import com.young.xjyq.entity.Tag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/11 21:29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("视频列表数据传输对象")
public class VideoListDto {

    @ApiModelProperty("视频ID")
    private Long videoId;

    @ApiModelProperty("视频封面图")
    private String coverImg;

    @ApiModelProperty("入库时间")
    private String createTime;

    @ApiModelProperty("标签")
    private List<Tag> tags;

    @ApiModelProperty("视频链接")
    private String url;

    @ApiModelProperty("视频链接")
    private String sourceUrl;

    @ApiModelProperty("所属来源")
    private String author;

    @ApiModelProperty("违规状态")
    private Integer status;

    @ApiModelProperty("人脸识别任务ID")
    private String faceTaskId;

    @ApiModelProperty("文本识别任务ID")
    private String ocrTaskId;

    @ApiModelProperty("人脸识别状态")
    private Integer faceStatus;

    @ApiModelProperty("文本识别状态")
    private Integer ocrStatus;

    @ApiModelProperty("文本违规状态")
    private List<Tag> illegal;
}
