package com.young.xjyq.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author error-codes【BayMax】
 * @see <a href="www.error-codes.xyz">BayMax Blog</a>
 * @since 2022/9/9 15:22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    private Long personId;

    private String avatar;

    private String name;

    private List<String> tags;

    private String remark;
}
