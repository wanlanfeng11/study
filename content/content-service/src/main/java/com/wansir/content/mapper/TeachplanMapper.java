package com.wansir.content.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansir.content.model.dto.TeachplanDto;
import com.wansir.content.model.pojo.Teachplan;

import java.util.List;

/**
 * <p>
 * 课程计划 Mapper 接口
 * </p>
 *
 * @author wansir
 */
public interface TeachplanMapper extends BaseMapper<Teachplan> {

    /**
     * @description 查询某课程的课程计划，组成树型结构
     * @param courseId
     * @return com.wansir.content.model.dto.TeachplanDto
     */
    public List<TeachplanDto> selectTreeNodes(long courseId);

}
