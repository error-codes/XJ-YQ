package com.young.xjyq.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/11 13:10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo<T> {

    private Integer page;

    private Integer pageSize;

    private Integer count;

    private List<T> dataList;
}
