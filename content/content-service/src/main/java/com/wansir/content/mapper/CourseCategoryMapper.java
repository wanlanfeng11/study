package com.wansir.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansir.content.model.dto.CourseCategoryTreeDto;
import com.wansir.content.model.pojo.CourseCategory;

import java.util.List;

/**
 * <p>
 * 课程分类 Mapper 接口
 * </p>
 *
 * @author wansir
 */
public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {

    List<CourseCategoryTreeDto> selectTreeNodes(String id);
}
