package com.wansir.content.model.dto;

import com.wansir.content.model.pojo.Teachplan;
import com.wansir.content.model.pojo.TeachplanMedia;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @description 课程计划树型结构dto
 */
@Data
@ToString
public class TeachplanDto extends Teachplan {

  //课程计划关联的媒资信息
  TeachplanMedia teachplanMedia;

  //子结点
  List<TeachplanDto> teachPlanTreeNodes;

}