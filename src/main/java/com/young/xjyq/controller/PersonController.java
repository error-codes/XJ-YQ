package com.young.xjyq.controller;

import com.young.xjyq.common.PageInfo;
import com.young.xjyq.common.Result;
import com.young.xjyq.dto.PersonDetailDto;
import com.young.xjyq.dto.PersonDto;
import com.young.xjyq.entity.Tag;
import com.young.xjyq.service.PersonService;
import com.young.xjyq.vo.UpdatePersonVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author error-codes【BayMax】
 * @see <a href="www.error-codes.xyz">BayMax Blog</a>
 * @since 2022/9/9 16:07
 */
@Api(tags = "人物管理")
@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/readPersonById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "人物ID", dataType = "Integer", dataTypeClass = Integer.class)
    })
    @ApiOperation("根据人物ID获取人物详情")
    public Result<PersonDetailDto> readPersonById(@RequestParam @NotNull Integer id) {
        try {
            return new Result<>(200, "查询成功", personService.readPersonById(id));
        } catch (Exception e) {
            return new Result<>(204, "查询失败", null);
        }
    }

    @GetMapping("/readAllPerson")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "Integer", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value = "装载数量", dataType = "Integer", dataTypeClass = Integer.class)
    })
    @ApiOperation("获取人物列表")
    public Result<PageInfo<PersonDto>> readAllPerson(@RequestParam(defaultValue = "1") Integer page,
                                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            return new Result<>(200, "查询成功", personService.readAllPerson(page, pageSize));
        } catch (Exception e) {
            return new Result<>(204, "查询失败", null);
        }
    }

    @PostMapping("/updatePersonById")
    @ApiOperation("根据人物ID修改人物信息")
    public Result<?> updatePersonById(@NotNull UpdatePersonVo updatePersonVo) {
        int result = personService.updatePersonById(updatePersonVo);
        if (result > 0) {
            return new Result<>(200, "修改成功", null);
        } else {
            return new Result<>(204, "修改失败", null);
        }
    }

    @PostMapping("/batchDeletePerson")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "人物ID列表", dataType = "Integer", dataTypeClass = Integer.class, allowMultiple = true)
    })
    @ApiOperation("批量删除人物")
    public Result<?> batchDeletePerson(@RequestParam @NotEmpty List<Integer> ids) {
        int result = personService.batchDeletePerson(ids);
        if (result > 0) {
            return new Result<>(200, "删除成功", null);
        } else {
            return new Result<>(204, "删除失败", null);
        }
    }

    @PostMapping("/createPerson")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "personName", value = "人物名称", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "tags", value = "人物标签", dataType = "Integer", dataTypeClass = Integer.class, allowMultiple = true),
            @ApiImplicitParam(name = "remark", value = "备注", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "image", value = "头像", dataType = "MultipartFile", dataTypeClass = MultipartFile.class)
    })
    @ApiOperation("新增人物")
    public Result<?> createPerson(@RequestParam @NotBlank String personName,
                                  @RequestParam List<Integer> tags,
                                  @RequestParam String remark,
                                  @RequestPart @NotNull MultipartFile image) {
        int result = personService.createPerson(personName, tags, remark, image);
        if (result > 0) {
            return new Result<>(200, "新增成功", null);
        } else {
            return new Result<>(204, "新增失败", null);
        }
    }

    @PostMapping("/updateAvatar")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "personId", value = "人物ID", dataType = "Integer", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "image", value = "头像", dataType = "MultipartFile", dataTypeClass = MultipartFile.class)
    })
    @ApiOperation("更新人物头像")
    public Result<?> updateAvatar(@RequestParam @NotNull Integer personId,
                                  @RequestPart @NotNull MultipartFile image) {
        int result = personService.updateAvatar(personId, image);
        if (result > 0) {
            return new Result<>(200, "修改成功", null);
        } else {
            return new Result<>(204, "修改失败", null);
        }
    }

    @PostMapping("/deleteAvatar")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "头像ID列表", dataType = "Integer", dataTypeClass = Integer.class, allowMultiple = true),
            @ApiImplicitParam(name = "personId", value = "人物ID", dataType = "Integer", dataTypeClass = Integer.class)
    })
    @ApiOperation("删除人物头像")
    public Result<?> deleteAvatar(@RequestParam List<Integer> ids,
                                  @RequestParam Integer personId) {
        int result = personService.deleteAvatar(ids, personId);
        if (result > 0) {
            return new Result<>(200, "删除成功", null);
        } else {
            return new Result<>(204, "删除失败", null);
        }
    }

    @PostMapping("/readAllTag")
    @ApiOperation("获取所有标签")
    public Result<List<Tag>> readAllTag() {
        return new Result<>(200, "修改成功", personService.readAllTag());
    }
}
