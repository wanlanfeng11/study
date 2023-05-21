package com.wansir.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.SelectList;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansir.base.exception.MyException;
import com.wansir.base.utils.BeanCopyUtils;
import com.wansir.content.mapper.CourseBaseMapper;
import com.wansir.content.mapper.CourseTeacherMapper;
import com.wansir.content.model.dto.AddCourseTeacherDto;
import com.wansir.content.model.dto.CourseBaseInfoDto;
import com.wansir.content.model.pojo.CourseBase;
import com.wansir.content.model.pojo.CourseTeacher;
import com.wansir.content.service.CourseBaseService;
import com.wansir.content.service.CourseTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程-教师关系表 服务实现类
 * </p>
 *
 * @author wansir
 */
@Slf4j
@Service
public class CourseTeacherServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacher> implements CourseTeacherService {


    @Autowired
    private CourseBaseMapper courseBaseMapper;

    @Autowired CourseTeacherMapper courseTeacherMapper;


    @Override
    public List<CourseTeacher> getTeachersByCourseId(Long courseId) {
        LambdaQueryWrapper<CourseTeacher> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CourseTeacher::getCourseId, courseId);
        List<CourseTeacher> courseTeachers = this.list(lqw);
        return courseTeachers;
    }

    @Override
    public CourseTeacher addTeacher(AddCourseTeacherDto addCourseTeacherDto) {
        //查询课程是否存在
        CourseBase courseBase = courseBaseMapper.selectById(addCourseTeacherDto.getCourseId());
        if(courseBase == null){
            MyException.cast("添加失败，课程不存在！");
        }
        //添加老师
        CourseTeacher courseTeacher = BeanCopyUtils.copyBean(addCourseTeacherDto, CourseTeacher.class);
        courseTeacherMapper.insert(courseTeacher);
        return courseTeacher;
    }

    @Override
    public CourseTeacher updateTeacher(CourseTeacher courseTeacher) {
        //查询老师是否存在
        CourseTeacher teacher = courseTeacherMapper.selectById(courseTeacher.getId());
        if(teacher == null){
            MyException.cast("老师不存在，无法修改！");
        }
        courseTeacherMapper.updateById(courseTeacher);
        return courseTeacher;
    }

    @Override
    public void deleteCourseTeacher(Long courseId, Long teacherId) {
        LambdaQueryWrapper<CourseTeacher> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CourseTeacher::getCourseId, courseId);
        lqw.eq(CourseTeacher::getId, teacherId);
        CourseTeacher courseTeacher = courseTeacherMapper.selectOne(lqw);
        if(courseTeacher == null){
            MyException.cast("课程不存在，删除失败！");
        }
        courseTeacherMapper.delete(lqw);
    }


}
