package com.young.xjyq.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/12 19:48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("人脸识别数据传输对象")
public class FaceDto {

    private List<PersonDto> persons;
}
