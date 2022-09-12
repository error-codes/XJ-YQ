package com.young.xjyq.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/11 21:03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultPage<T> {

    private Integer error;

    private Integer totalCount;

    private List<T> data;
}
