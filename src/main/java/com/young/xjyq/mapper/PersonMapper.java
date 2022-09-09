package com.young.xjyq.mapper;

import com.young.xjyq.entity.Person;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author error-codes【BayMax】
 * @see <a href="www.error-codes.xyz">BayMax Blog</a>
 * @since 2022/9/9 17:34
 */
@Mapper
public interface PersonMapper {

    @Select("select * from person where id = #{id} and deleted = 0")
    Person readPersonById(Long id);

    @Select("select * from person where deleted = 0")
    List<Person> readAllPerson();

    @Update("update person set name = #{name}, avatar = #{avatar}, updateTime = #{updateTime}, ifnull(deleted, deleted = #{})}")
    int updatePersonById(Long id, Person person);

    int batchDeletePerson(List<Long> ids);

    int createPerson(Person person);
}
