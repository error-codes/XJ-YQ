package com.young.xjyq.dto.apiResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/11 21:04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoGatherDto {

    private String author;
    private Integer click;
    private Integer comments;
    private String content;
    private Integer crSec;
    private String esIndex;
    private String formatGatherTime;
    private String formatPublishTime;
    private String gatherTimeFormat;
    private String gathertime;
    private String gathertimeString;
    private Integer groupId;
    private Long inserttime;
    private String language;
    private String publishTimeFormat;
    private Long publishtime;
    private String publishtimeString;
    private String pubtime;
    private Integer repeatCount;
    private Integer reply;
    private String sen;
    private Integer siteId;
    private String siteName;
    private String title;
    private String touXiang;
    private String url;
    private String urlhash;
    private String userId;
    private String weiboId;
    private String summary;
    private String site_url;
}
