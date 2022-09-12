package com.young.xjyq.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/11 19:04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultPageList<T> {

    private Integer code;

    private Integer count;

    private Object next;

    private Object previous;

    private String msg;

    private List<T> data;
}
