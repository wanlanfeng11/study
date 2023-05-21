package com.wansir.content.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description 添加课程dto
 */
@Data
@ApiModel(value="UpdateCourseDto", description="修改课程基本信息")
public class UpdateCourseDto extends AddCourseDto {

 @ApiModelProperty(value = "课程id", required = true)
 private Long id;

}