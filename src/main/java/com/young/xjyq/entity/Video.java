package com.young.xjyq.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author error-codes【BayMax】
 * @see <a href="www.error-codes.xyz">BayMax Blog</a>
 * @since 2022/9/9 14:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Video {

    private Long videoId;

    private LocalDateTime createTime;

    private List<String> tags;

    private String url;

    private String source;

    private Integer status;
}
