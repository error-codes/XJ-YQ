package com.young.xjyq.service;

import com.dtflys.forest.annotation.*;
import com.young.xjyq.common.Result;
import com.young.xjyq.common.ResultPageList;
import com.young.xjyq.dto.apiResult.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/11 18:50
 */
@Service
public interface RemotePersonService {

    @Get(url = "http://192.17.1.20:9898/jtface/v1/person/query_person/")
    Result<ReadPersonDto> readPersonById(@Query("person_id") Integer id);

    @Get(url = "http://192.17.1.20:9898/jtface/v1/person/list/")
    ResultPageList<ReadPersonListDto> readAllPerson(@Query("offset") Integer page,
                                                    @Query("limit") Integer pageSize,
                                                    @Query("repo_id") String repoId);

    @Post(url = "http://192.17.1.20:9898/jtface/v1/person/{personId}/repair_stranger/")
    Result<ReadPersonDto> updatePersonById(@Var("personId") Integer personId,
                                           @Body("person_name") String personName,
                                           @Body("repo_id") String repoId);

    @Delete(url = "http://192.17.1.20:9898/jtface/v1/person/list_destroy/")
    Result<DeletePersonDto> batchDeletePerson(@JSONBody("person_ids") List<Integer> ids);

    @Post(url = "http://192.17.1.20:9898/jtface/v1/person/")
    Result<CreatePersonDto> createPerson(@Body("person_name") String personName,
                                         @Body("images") String imageIndex,
                                         @DataFile("image0") MultipartFile image,
                                         @Body("repo_id") String repoId);

    @Post(url = "http://192.17.1.20:9898/jtface/v1/faceimg/")
    Result<UpdateAvatarDto> updateAvatar(@Body("person_id") Integer personId,
                                         @Body("images") String imageIndex,
                                         @DataFile("image0") MultipartFile image);

    @Delete(url = "http://192.17.1.20:9898/jtface/v1/faceimg/list_destroy/")
    Result<DeleteAvatarDto> deleteAvatar(@JSONBody("face_ids") List<Integer> ids);
}
