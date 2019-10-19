package com.xuecheng.search;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.search.service.EsCourseService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class TestEsCourseService {

    @Autowired
    private EsCourseService esCourseService;


    @Test
    public void list() {
        CourseSearchParam courseSearchParam = new CourseSearchParam();
        courseSearchParam.setKeyword("开发");
        QueryResponseResult<CoursePub> list = esCourseService.list(1, 10, courseSearchParam);
        log.info(list.toString());
    }

}
