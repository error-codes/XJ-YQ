package com.young.xjyq.dto.apiResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/12 12:56
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaceTaskResultDto {

    private String video_id;
    private String task_status;
    private String task_progress;
    private String start_time;
    private String end_time;
}
