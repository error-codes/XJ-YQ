package com.young.xjyq.mapper;

import com.young.xjyq.entity.Person;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/10 17:24
 */
@Mapper
public interface PersonMapper {

    int createPerson(Integer personId, String tags, String remark);

    int deletePerson(List<Integer> personIds);

    int updatePerson(Integer personId, String tags, String remark);

    Person readPersonById(Integer personId);

    List<Person> readAllPerson();
}
