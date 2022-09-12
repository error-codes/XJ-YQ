package com.young.xjyq.dto;

import com.young.xjyq.entity.Tag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
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
    private Date createTime;

    private List<Tag> tags;

    @ApiModelProperty("视频链接")
    private String url;

}
