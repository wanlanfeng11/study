package com.wansir.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wansir.content.model.dto.CourseCategoryTreeDto;
import com.wansir.content.model.pojo.CourseCategory;

import java.util.List;

public interface CourseCategoryService extends IService<CourseCategory> {
    /**
     * 课程分类树形结构查询
     *
     * @return
     */
    public List<CourseCategoryTreeDto> queryTreeNodes(String id);
}