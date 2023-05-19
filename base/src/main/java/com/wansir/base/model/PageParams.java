package com.wansir.base.model;


/**
 * @author wanlanfeng
 * @version 1.0
 * @description 分页查询通用参数
 * @date 2023/5/18 19:22
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ApiModel(value = "分页参数类")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PageParams {

    //当前页码
    @ApiModelProperty("当前页码")
    private Long pageNo = 1L;

    //每页记录数默认值
    @ApiModelProperty("每页记录数")
    private Long pageSize =10L;




}