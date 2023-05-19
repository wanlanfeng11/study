package com.wansir.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wansir.base.model.PageParams;
import com.wansir.content.model.dto.AddCourseDto;
import com.wansir.content.model.dto.CourseBaseInfoDto;
import com.wansir.content.model.dto.PageResult;
import com.wansir.content.model.dto.QueryCourseParamsDto;
import com.wansir.content.model.pojo.CourseBase;

/**
 * 课程基本信息(CourseBase)表服务接口
 *
 * @author makejava
 * @since 2023-05-18 18:30:01
 */
public interface CourseBaseService extends IService<CourseBase> {


    /**
     * @description 课程查询接口
     * @param pageParams 分页参数
     * @param queryCourseParamsDto 条件条件
     */
    PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);

    /**
     * @description 添加课程基本信息
     * @param companyId  教学机构id
     * @param addCourseDto  课程基本信息
     * @return com.xuecheng.content.model.dto.CourseBaseInfoDto
     */
    CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);





}

