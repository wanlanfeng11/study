package com.wansir.content.service;

import com.wansir.content.model.dto.SaveTeachplanDto;
import com.wansir.content.model.dto.TeachplanDto;

import java.util.List;

/**
 * @description 课程基本信息管理业务接口
 */
public interface TeachplanService {

/**
 * @description 查询课程计划树型结构
 * @param courseId  课程id
 * @return List<TeachplanDto>
*/
 public List<TeachplanDto> findTeachplanTree(long courseId);


 /**
  * @description 保存课程计划
  * @param teachplanDto  课程计划信息
  */
 public void saveTeachplan(SaveTeachplanDto teachplanDto);

 /**
  * 删除课程计划
  * @param teachPlanId 课程计划id
  */
 void deleteTeachplan(Long teachPlanId);

 /**
  * 修改课程顺序
  * @param move
  * @param teachplanId
  */
 void changeOrder(String move, Long teachplanId);
}