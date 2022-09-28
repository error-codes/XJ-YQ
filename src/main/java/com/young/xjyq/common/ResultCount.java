package com.young.xjyq.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
public class ResultCount<T> {

    private Integer code;

    private String msg;

    private List<T> data;

    private Integer count;
}
