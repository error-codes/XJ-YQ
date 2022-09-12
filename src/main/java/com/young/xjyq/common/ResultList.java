package com.young.xjyq.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/12 12:17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultList<T> {

    private Integer code;

    private String msg;

    private List<T> data;
}
