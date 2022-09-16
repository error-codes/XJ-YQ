package com.young.xjyq.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 响应结果结构体
 *
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/10 14:17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("响应结果集")
public class Result<T> {

    @ApiModelProperty("响应码")
    private Integer code;

    @ApiModelProperty("提示信息")
    private String msg;

    @ApiModelProperty("响应数据")
    private T data;

}
