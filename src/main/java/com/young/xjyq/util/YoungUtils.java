package com.young.xjyq.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/10 18:28
 */
public class YoungUtils {

    public static String mergeTagList(List<Integer> tags) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer tag : tags) {
            stringBuilder.append(tag).append("&");
        }
        return stringBuilder.toString();
    }

    public static List<Integer> getTag(String tags) {
        if (StringUtils.isBlank(tags)) {
            return null;
        }
        String[] tagIds = tags.split("&");
        List<Integer> tagIdList = new ArrayList<>(10);
        for (String tagId : tagIds) {
            tagIdList.add(Integer.parseInt(tagId));
        }
        return tagIdList;
    }

    public static List<Integer> contains(List<Integer> tagList, String tags) {
        String[] tagIds = tags.split("&");
        List<Integer> tagIdList = new ArrayList<>(10);
        for (String tagId : tagIds) {
            tagIdList.add(Integer.parseInt(tagId));
        }
        for (Integer tag : tagIdList) {
            if (!tagList.contains(tag)) {
                tagList.add(tag);
            }
        }
        return tagList;
    }
}
