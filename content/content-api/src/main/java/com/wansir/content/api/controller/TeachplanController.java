package com.wansir.content.api.controller;

import com.wansir.content.model.dto.SaveTeachplanDto;
import com.wansir.content.model.dto.TeachplanDto;
import com.wansir.content.service.TeachplanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description 课程计划编辑接口
 */
 @Api(value = "课程计划编辑接口",tags = "课程计划编辑接口")
 @RestController
public class TeachplanController {

     @Autowired
     private TeachplanService teachplanService;


    @ApiOperation("查询课程计划树形结构")
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(@ApiParam(value = "课程id") @PathVariable Long courseId){
        return teachplanService.findTeachplanTree(courseId);
    }


    @ApiOperation("课程计划创建或修改")
    @PostMapping("/teachplan")
    public void saveTeachplan( @RequestBody SaveTeachplanDto teachplan){
        teachplanService.saveTeachplan(teachplan);
    }

    @ApiOperation("删除课程计划")
    @DeleteMapping("teachplan/{id}")
    public void deleteTeachplan( @PathVariable(value = "id", required = true) Long id){
        teachplanService.deleteTeachplan(id);
    }


    @ApiOperation(value = "修改课程计划顺序的接口")
    @PostMapping("/teachplan/{move}/{teachplanId}")
    public void changeOrder(@PathVariable String move, @PathVariable Long teachplanId){
        teachplanService.changeOrder(move, teachplanId);

    }



}