package com.young.xjyq.mapper;

import com.young.xjyq.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/10 18:54
 */
@Mapper
public interface TagMapper {

    List<Tag> readAllTag();

    List<Tag> readTagByIds(List<Integer> tagIds);
}
