package com.wansir.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wansir.content.model.dto.AddCourseTeacherDto;
import com.wansir.content.model.pojo.CourseTeacher;

import java.util.List;

/**
 * <p>
 * 课程-教师关系表 服务类
 * </p>
 *
 * @author wansir
 * @since 2023-05-18
 */
public interface CourseTeacherService extends IService<CourseTeacher> {


    /**
     * 根据课程id查询老师
     * @param courseId
     * @return
     */
    List<CourseTeacher> getTeachersByCourseId(Long courseId);

    CourseTeacher addTeacher(AddCourseTeacherDto addCourseTeacherDto);

    /**
     * 修改老师信息
     * @param courseTeacher
     * @return
     */
    CourseTeacher updateTeacher(CourseTeacher courseTeacher);

    /**
     * 删除老师
     * @param courseId
     * @param teacherId
     */
    void deleteCourseTeacher(Long courseId, Long teacherId);

    /**
     * 添加课程老师
     * @param courseTeacher
     */
    

}
