package com.young.xjyq.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author error-codes【BayMax】
 * @see <a href="www.error-codes.xyz">BayMax Blog</a>
 * @since 2022/9/13 10:13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ocr {

    @ApiModelProperty("时间帧")
    private Float timeFrame;

    @ApiModelProperty("文本内容")
    private String text;
}
