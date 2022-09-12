package com.young.xjyq.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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

    private Long id;

    private Date createTime;

    private Date updateTime;

    private String tags;

    private String url;

    private String source;

    private Integer status;

    private Integer deleted;

    private String faceTaskId;

    private String ocrTaskId;

    private Integer faceStatus;

    private Integer ocrStatus;
}
