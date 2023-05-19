package com.wansir.content.api.controller;

import com.wansir.base.model.PageParams;
import com.wansir.content.model.dto.AddCourseDto;
import com.wansir.content.model.dto.CourseBaseInfoDto;
import com.wansir.content.model.dto.PageResult;
import com.wansir.content.model.dto.QueryCourseParamsDto;
import com.wansir.content.model.pojo.CourseBase;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.wansir.content.service.CourseBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 课程基本信息 前端控制器
 */
@Slf4j
@Api(tags = "课程信息相关接口")
@RestController
public class CourseBaseController {

    @Autowired
    private CourseBaseService  courseBaseService;

    @ApiOperation("课程查询接口")
    @PostMapping("/course/list")
    public PageResult<CourseBase> list(PageParams pageParams, @RequestBody QueryCourseParamsDto queryCourseParams){
        PageResult<CourseBase> pageResult = courseBaseService.queryCourseBaseList(pageParams, queryCourseParams);
        return pageResult;
    }

    @ApiOperation("新增课程基础信息")
    @PostMapping("/course")
    @Validated
    public CourseBaseInfoDto createCourseBase(@RequestBody AddCourseDto addCourseDto){
        //机构id，由于认证系统没有上线暂时硬编码
        Long companyId = 1232141425L;
        return courseBaseService.createCourseBase(companyId,addCourseDto);
    }


}
