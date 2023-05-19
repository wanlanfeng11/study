package com.wansir.content.model.dto;

import com.wansir.content.model.pojo.CourseCategory;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CourseCategoryTreeDto extends CourseCategory implements Serializable {

  List<CourseCategoryTreeDto> childrenTreeNodes;
}