package com.young.xjyq.dto.apiResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/10 17:45
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeletePersonDto {

    private List<Integer> person_id;

}
