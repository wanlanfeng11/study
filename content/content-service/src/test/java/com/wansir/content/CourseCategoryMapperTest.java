package com.wansir.content;


import com.wansir.content.mapper.CourseCategoryMapper;
import com.wansir.content.model.dto.CourseCategoryTreeDto;
import com.wansir.content.model.pojo.CourseCategory;
import com.wansir.content.service.CourseCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/19 12:28
 */
@SpringBootTest
public class CourseCategoryMapperTest {
    @Autowired
    private CourseCategoryService courseCategoryService;

    @Test
    void selectTreeNodesTest(){
        long start = System.currentTimeMillis();
        List<CourseCategoryTreeDto> result = courseCategoryService.queryTreeNodes("1");
        long end = System.currentTimeMillis() - start;
        System.out.println(end);
    }

    @Test
    void selectTreeNodesTest2(){
        long start = System.currentTimeMillis();
        List<CourseCategory> list = courseCategoryService.list();

        long end = System.currentTimeMillis() - start;
        System.out.println(end);
    }
}
