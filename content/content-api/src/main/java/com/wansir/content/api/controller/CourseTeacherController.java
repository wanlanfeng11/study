package com.wansir.content.api.controller;


import com.wansir.content.model.dto.AddCourseTeacherDto;
import com.wansir.content.model.pojo.CourseTeacher;
import com.wansir.content.service.CourseTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/20 9:21
 */
@RestController
@RequestMapping("/courseTeacher")
@Api(tags = "课程老师相关接口")
public class CourseTeacherController {
    @Autowired
    private CourseTeacherService courseTeacherService;


    @ApiOperation("根据课程id查询老师信息")
    @GetMapping("/list/{courseId}")
    public List<CourseTeacher> getTeachersByCourseId(@PathVariable Long courseId){
        List<CourseTeacher> courseTeachers = courseTeacherService.getTeachersByCourseId(courseId);
        return courseTeachers;
    }

    @ApiOperation("添加老师")
    @PostMapping()
    public CourseTeacher addTeachers(@RequestBody AddCourseTeacherDto addCourseTeacherDto){
        CourseTeacher courseTeacher = courseTeacherService.addTeacher(addCourseTeacherDto);
        return courseTeacher;
    }


    @ApiOperation("修改老师")
    @PutMapping()
    public CourseTeacher updateTeachers(@RequestBody CourseTeacher courseTeacher){
        CourseTeacher result = courseTeacherService.updateTeacher(courseTeacher);
        return result;
    }

    @DeleteMapping("/course/{courseId}/{teacherId}")
    public void deleteCourseTeacehr(@PathVariable Long courseId, @PathVariable Long teacherId){
        courseTeacherService.deleteCourseTeacher(courseId, teacherId);
    }



}
