package com.young.xjyq.dto.apiResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/12 12:53
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OcrTaskResultDto {

    private String task_id;
    private String task_status;
}
