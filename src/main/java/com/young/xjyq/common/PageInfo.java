package com.young.xjyq.common;

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
 * @since 2022/9/11 13:10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("页码响应结果集")
public class PageInfo<T> {

    @ApiModelProperty("页码")
    private Integer page;

    @ApiModelProperty("装载数量")
    private Integer pageSize;

    @ApiModelProperty("总数据量")
    private Integer count;

    @ApiModelProperty("数据列表")
    private List<T> dataList;
}
