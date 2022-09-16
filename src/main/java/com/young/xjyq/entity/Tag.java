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
 * @since 2022/9/10 18:55
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("标签")
public class Tag {

    @ApiModelProperty("标签ID")
    private Integer id;

    @ApiModelProperty("标签名")
    private String tag;
}
