package com.young.xjyq.service.impl;

import com.young.xjyq.common.PageInfo;
import com.young.xjyq.common.Result;
import com.young.xjyq.common.ResultPageList;
import com.young.xjyq.dto.PersonDetailDto;
import com.young.xjyq.dto.PersonDto;
import com.young.xjyq.dto.apiResult.*;
import com.young.xjyq.entity.Person;
import com.young.xjyq.entity.Tag;
import com.young.xjyq.mapper.PersonMapper;
import com.young.xjyq.mapper.TagMapper;
import com.young.xjyq.service.PersonService;
import com.young.xjyq.service.RemotePersonService;
import com.young.xjyq.util.YoungUtils;
import com.young.xjyq.vo.UpdatePersonVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author error-codes【BayMax】
 * @see <a href="www.error-codes.xyz">BayMax Blog</a>
 * @since 2022/9/9 16:14
 */
@Service
public class PersonServiceImpl implements PersonService {

    @Value("${xinrui.api.repoId}")
    private String repoId;

    private final PersonMapper personMapper;
    private final TagMapper tagMapper;
    private final RemotePersonService remotePersonService;

    @Autowired
    public PersonServiceImpl(PersonMapper personMapper,
                             TagMapper tagMapper,
                             RemotePersonService remotePersonService) {
        this.personMapper = personMapper;
        this.tagMapper = tagMapper;
        this.remotePersonService = remotePersonService;
    }

    @Override
    public PersonDetailDto readPersonById(Integer id) {
        Result<ReadPersonDto> result = remotePersonService.readPersonById(id);

        if (result.getCode() != 0) {
            throw new RuntimeException();
        }
        Person person = personMapper.readPersonById(id);
        ReadPersonDto readPersonDto = result.getData();
        return new PersonDetailDto(readPersonDto.getId(),
                readPersonDto.getPerson_name(),
                readPersonDto.getFace_list(),
                Objects.isNull(person) ? null : tagMapper.readTagByIds(YoungUtils.getTag(person.getTags())),
                Objects.isNull(person) ? null : person.getRemark());
    }

    @Override
    public PageInfo<PersonDto> readAllPerson(Integer page, Integer pageSize) {
        List<Person> personList = personMapper.readAllPerson();

        List<PersonDto> personDtos = new ArrayList<>();
        for (Person person : personList) {
            personDtos.add(new PersonDto(person.getId(),
                    person.getPersonName(),
                    person.getFaceUrl(),
                    tagMapper.readTagByIds(YoungUtils.getTag(person.getTags())),
                    person.getRemark()));
        }

        return new PageInfo<>(page, pageSize, personList.size(), personDtos);
    }

    @Override
    public int updatePersonById(UpdatePersonVo updatePersonVo) {
        Result<ReadPersonDto> result = remotePersonService.updatePersonById(updatePersonVo.getPersonId(),
                updatePersonVo.getPersonName(),
                repoId);

        if (result.getCode() != 0) {
            return -1;
        }

        return personMapper.updatePerson(updatePersonVo.getPersonId(),
                YoungUtils.mergeTagList(updatePersonVo.getTags()),
                updatePersonVo.getRemark(),
                null);

    }

    @Override
    public int batchDeletePerson(List<Integer> ids) {
        Result<DeletePersonDto> result = remotePersonService.batchDeletePerson(ids);

        if (result.getCode() != 0) {
            return -1;
        }
        return personMapper.deletePerson(result.getData().getPerson_id());
    }

    @Override
    public int createPerson(String personName, List<Integer> tags, String remark, MultipartFile image) {
        Result<CreatePersonDto> result = remotePersonService.createPerson(personName,
                "image0",
                image,
                repoId);

        if (result.getCode() != 0) {
            return -1;
        }
        List<ReadPersonDto.Face> faceList = remotePersonService.readPersonById(result.getData().getPerson_id()).getData().getFace_list();
        return personMapper.createPerson(result.getData().getPerson_id(),
                YoungUtils.mergeTagList(tags),
                remark,
                personName,
                faceList.isEmpty() ? null : faceList.get(0).getFace_url());
    }

    @Override
    public int updateAvatar(Integer personId, MultipartFile image) {
        Result<UpdateAvatarDto> result = remotePersonService.updateAvatar(personId, "image0", image);

        if (result.getCode() != 0) {
            throw new RuntimeException();
        }
        List<ReadPersonDto.Face> faceList = remotePersonService.readPersonById(personId).getData().getFace_list();
        return personMapper.updatePerson(personId, null, null, faceList.isEmpty() ? null : faceList.get(0).getFace_url());
    }

    @Override
    public int deleteAvatar(List<Integer> ids, Integer personId) {
        Result<DeleteAvatarDto> result = remotePersonService.deleteAvatar(ids);

        if (result.getCode() != 0) {
            return -1;
        }
        List<ReadPersonDto.Face> faceList = remotePersonService.readPersonById(personId).getData().getFace_list();
        return personMapper.updatePerson(personId, null, null, faceList.isEmpty() ? null : faceList.get(0).getFace_url());
    }

    @Override
    public List<Tag> readAllTag() {
        return tagMapper.readAllTag();
    }

}
