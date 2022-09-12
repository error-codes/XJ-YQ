package com.young.xjyq.dto.apiResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/11 19:42
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteAvatarDto {

    private Integer suc_face_num;
    private List<Integer> suc_face_ids;
}
