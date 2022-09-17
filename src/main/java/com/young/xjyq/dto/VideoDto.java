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
@ApiModel("视频数据传输对象")
public class VideoDto {

    @ApiModelProperty("视频ID")
    private Long videoId;

    @ApiModelProperty("入库时间")
    private String createTime;

    private List<Tag> tags;

    @ApiModelProperty("视频链接")
    private String url;

    @ApiModelProperty("视频链接")
    private String sourceUrl;

    @ApiModelProperty("所属来源")
    private String author;

    @ApiModelProperty("违规状态<br/>-1 待分析<br/>" +
            "0 未违规<br/>" +
            "1 色情<br/>" +
            "2 违禁<br/>" +
            "3 广告<br/>" +
            "4 暴恐<br/>" +
            "5 涉政<br/>" +
            "6 内容风险<br/> ")
    private List<Integer> status;

    @ApiModelProperty("人脸识别任务ID")
    private String faceTaskId;

    @ApiModelProperty("文本识别任务ID")
    private String ocrTaskId;

    @ApiModelProperty("人脸识别状态")
    private Integer faceStatus;

    @ApiModelProperty("文本识别状态")
    private Integer ocrStatus;
}
