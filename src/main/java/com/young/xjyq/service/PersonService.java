package com.young.xjyq.service;

import com.young.xjyq.entity.Person;

import java.util.List;

/**
 * @author error-codes【BayMax】
 * @see <a href="www.error-codes.xyz">BayMax Blog</a>
 * @since 2022/9/9 16:14
 */
public interface PersonService {

    Person readPersonById(Long id);

    List<Person> readAllPerson();

    int updatePersonById(Long id);

    int batchDeletePerson(List<Long> ids);

    int createPerson(Person person);
}
