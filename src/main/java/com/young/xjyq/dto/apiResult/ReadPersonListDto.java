package com.young.xjyq.dto.apiResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/11 19:05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadPersonListDto implements Serializable {

    private Integer id;
    private Integer repo_id;
    private String person_name;
    private String person_card;
    private String gender;
    private String person_phone;
    private String person_exdescriptions;
    private Boolean unique_person_control;
    private String face_url;
}
