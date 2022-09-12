package com.young.xjyq.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/10 14:41
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("人物视图对象")
public class PersonVo {

    @NotBlank
    @ApiModelProperty("人物名称")
    private String personName;

    @ApiModelProperty("人物标签")
    private List<Integer> tags;

    @ApiModelProperty("备注")
    private String remark;

}
