package com.young.xjyq.dto;

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
 * @since 2022/9/12 11:59
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("文本识别")
public class OcrDto {

    private List<Ocr> ocrDtos;



}
