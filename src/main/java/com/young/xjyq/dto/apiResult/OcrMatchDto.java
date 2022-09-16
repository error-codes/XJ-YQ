package com.young.xjyq.dto.apiResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/12 11:57
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OcrMatchDto {

    private Integer id;
    private String stream_id;
    private String frame_id;
    private String timestamp;
    private String create_time;
    private String ocr_result;
    private Float intime;

}
