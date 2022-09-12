package com.young.xjyq.service;

import com.young.xjyq.common.PageInfo;
import com.young.xjyq.dto.PersonDto;
import com.young.xjyq.entity.Tag;
import com.young.xjyq.vo.UpdatePersonVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author error-codes【BayMax】
 * @see <a href="www.error-codes.xyz">BayMax Blog</a>
 * @since 2022/9/9 16:14
 */
public interface PersonService {

    PersonDto readPersonById(Integer id);

    PageInfo<PersonDto> readAllPerson(Integer page, Integer pageSize);

    int updatePersonById(UpdatePersonVo updatePersonVo);

    int batchDeletePerson(List<Integer> ids);

    int createPerson(String personName, List<Integer> tags, String remark, MultipartFile image);

    int updateAvatar(Integer personId, MultipartFile image);

    int deleteAvatar(List<Integer> ids);

    List<Tag> readAllTag();
}
