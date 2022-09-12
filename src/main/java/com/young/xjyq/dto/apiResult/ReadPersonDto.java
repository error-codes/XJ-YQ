package com.young.xjyq.dto.apiResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/11 18:59
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadPersonDto {

    private Integer id;
    private Integer repo_id;
    private String person_name;
    private String person_card;
    private String gender;
    private String person_phone;
    private String person_exdescriptions;
    private List<Face> face_list;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Face {
        private Integer face_id;
        private String face_url;
        private Boolean is_update;
        private Integer update_code;
    }
}
