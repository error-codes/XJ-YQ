package com.young.xjyq.dto.apiResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/12 11:35
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaceMatchDto {

    private Integer id;
    private String face_url;
    private String repo_id;
    private String stream_id;
    private String image;
    private Integer face_id;
    private Integer person_id;
    private String person_name;
    private String person_card;
    private String person_phone;
    private String timestamp;
    private String frame_id;
    private Double conf;
    private String org_image;
    private Integer x;
    private Integer y;
    private Integer w;
    private Integer h;
    private String create_time;
    private Float face_quality;
}
