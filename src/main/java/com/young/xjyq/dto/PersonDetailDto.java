package com.young.xjyq.dto;

import com.young.xjyq.dto.apiResult.ReadPersonDto;
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
 * @since 2022/9/10 19:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("人物数据传输对象")
public class PersonDetailDto {

    @ApiModelProperty("人员ID")
    private Integer id;

    @ApiModelProperty("姓名")
    private String personName;

    @ApiModelProperty("人脸图片地址列表")
    private List<ReadPersonDto.Face> faceUrl;

    @ApiModelProperty("人物标签")
    private List<Tag> tags;

    @ApiModelProperty("备注")
    private String remark;
}
