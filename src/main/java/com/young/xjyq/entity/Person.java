package com.young.xjyq.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/10 19:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("人物库")
public class Person {

    @ApiModelProperty("人员ID")
    private Long id;

    @ApiModelProperty("人物标签")
    private String tags;

    @ApiModelProperty("备注")
    private String remark;
}
