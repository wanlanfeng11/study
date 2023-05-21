package com.wansir.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wansir.base.exception.MyException;
import com.wansir.content.mapper.TeachplanMapper;
import com.wansir.content.mapper.TeachplanMediaMapper;
import com.wansir.content.model.dto.SaveTeachplanDto;
import com.wansir.content.model.dto.TeachplanDto;
import com.wansir.content.model.pojo.Teachplan;
import com.wansir.content.model.pojo.TeachplanMedia;
import com.wansir.content.service.TeachplanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description 课程计划service接口实现类
 */
 @Service
public class TeachplanServiceImpl implements TeachplanService {

  @Autowired
  TeachplanMapper teachplanMapper;

  @Autowired
  private TeachplanMediaMapper teachplanMediaMapper;
 @Override
 public List<TeachplanDto> findTeachplanTree(long courseId) {
  return teachplanMapper.selectTreeNodes(courseId);
 }

    @Transactional
    @Override
    public void saveTeachplan(SaveTeachplanDto teachplanDto) {

        //课程计划id
        Long id = teachplanDto.getId();
        //修改课程计划
        if(id!=null){
            Teachplan teachplan = teachplanMapper.selectById(id);
            BeanUtils.copyProperties(teachplanDto,teachplan);
            teachplanMapper.updateById(teachplan);
        }else{
            //取出同父同级别的课程计划数量
            int count = getTeachplanCount(teachplanDto.getCourseId(), teachplanDto.getParentid());
            Teachplan teachplanNew = new Teachplan();
            //设置排序号
            teachplanNew.setOrderby(count+1);
            BeanUtils.copyProperties(teachplanDto,teachplanNew);

            teachplanMapper.insert(teachplanNew);

        }

    }

    @Override
    @Transactional
    public void deleteTeachplan(Long teachPlanId) {
        //1.根据课程计划的id查询课程计划
        Teachplan teachplan = teachplanMapper.selectById(teachPlanId);
        //2.判断课程计划是否存在
        if(teachplan == null){
            //不存在就报错
            MyException.cast("该课程计划不存在");
        }
        //3.判断课程计划是否有子课程计划
        LambdaQueryWrapper<Teachplan> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Teachplan::getParentid, teachPlanId);

        Integer count = teachplanMapper.selectCount(lqw);
        if(count != 0){
            //如果子课程计划不为空就报错
            MyException.cast("课程计划信息还有子级信息，无法操作");
        }
        //4.删除课程计划
        teachplanMapper.deleteById(teachPlanId);
        //5.删除课程计划关联的视频
        LambdaQueryWrapper<TeachplanMedia> lq = new LambdaQueryWrapper<>();
        lq.eq(TeachplanMedia::getTeachplanId, teachPlanId);
        teachplanMediaMapper.delete(lq);
    }



    /**
     * @description 获取最新的排序号
     * @param courseId  课程id
     * @param parentId  父课程计划id
     * @return int 最新排序号
     */
    private int getTeachplanCount(long courseId,long parentId){
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId,courseId);
        queryWrapper.eq(Teachplan::getParentid,parentId);
        Integer count = teachplanMapper.selectCount(queryWrapper);
        return count;
    }


    @Override
    public void changeOrder(String move, Long teachplanId) {
        //1.根据课程计划id查询课程计划
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        if (teachplan == null) {
            //如果查询结果为空就抛出异常
            MyException.cast("该课程计划不存在");
        }
        //2.获取课程计划的orderBy
        Integer orderby = teachplan.getOrderby();
        //3.根据move参数判断上移还是下移
        if ("moveup".equals(move)) {
            //4.上移
            //4.1.判断课程计划是否位于最顶端，即orderBy=1，如果为1就不需要进行上移操作
            if (orderby != 1) {
                //如果不为1则进行上移操作
                //查询对应的需要进行下移操作的课程计划
                LambdaQueryWrapper<Teachplan> lqw = new LambdaQueryWrapper<>();
                lqw.eq(Teachplan::getParentid, teachplan.getParentid()).eq(Teachplan::getOrderby, orderby - 1);
                Teachplan teachplan1 = teachplanMapper.selectOne(lqw);
                //将对应的课程计划进行下移
                teachplan1.setOrderby(orderby);
                teachplanMapper.updateById(teachplan1);
                //将本课程计划进行上移
                teachplanMapper.updateById(teachplan.setOrderby(orderby - 1));
            }

        } else {
            //5.下移
            //查询orderby+1且parentid与本课程计划相等课程计划
            LambdaQueryWrapper<Teachplan> lqw = new LambdaQueryWrapper<>();
            lqw.eq(Teachplan::getParentid, teachplan.getParentid()).eq(Teachplan::getOrderby, orderby + 1);
            Teachplan teachplan1 = teachplanMapper.selectOne(lqw);
            //如果查询结果不为空，则意味着可以进行下移操作
            if (teachplan1 != null) {
                teachplanMapper.updateById(teachplan.setOrderby(orderby + 1));
                teachplanMapper.updateById(teachplan1.setOrderby(orderby));
            }

        }
    }
}