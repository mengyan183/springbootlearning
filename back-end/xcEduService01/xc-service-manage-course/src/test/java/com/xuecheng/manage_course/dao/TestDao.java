package com.xuecheng.manage_course.dao;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class TestDao {
    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    CourseMapper courseMapper;

    @Autowired
    private TeachplanMediaRepository teachplanMediaRepository;

    @Test
    public void testCourseBaseRepository(){
        Optional<CourseBase> optional = courseBaseRepository.findById("402885816240d276016240f7e5000002");
        if(optional.isPresent()){
            CourseBase courseBase = optional.get();
            log.info(JSON.toJSONString(courseBase));
        }

    }

    @Test
    public void testCourseMapper(){
        CourseBase courseBase = courseMapper.findCourseBaseById("402885816240d276016240f7e5000002");
        log.info(JSON.toJSONString(courseBase));
    }

    @Test
    public void findList(){
        CategoryNode list = categoryService.findList();
        log.info(list.toString());
    }

    @Test
    public void test() {
        String teachplanId = "40288581632b593e01632bd606480004";
        String courseId = "297e7c7c62b888f00162b8a7dec20000";
        String mediaFileOriginalName = "solr.avi";
        String mediaId = "5fbb79a2016c0eb609ecd0cd3dc48016";
        String mediaUrl = "5/f/5fbb79a2016c0eb609ecd0cd3dc48016/hls/5fbb79a2016c0eb609ecd0cd3dc48016.m3u8";
        Optional<TeachplanMedia> teachplanMediaOptional = teachplanMediaRepository.findById(teachplanId);
        // 如果查询不存在,创建新的对象
        TeachplanMedia one = teachplanMediaOptional.orElseGet(TeachplanMedia::new);
        //保存媒资信息与课程计划信息
        one.setTeachplanId(teachplanId);
        one.setCourseId(courseId);
        one.setMediaFileOriginalName(mediaFileOriginalName);
        one.setMediaId(mediaId);
        one.setMediaUrl(mediaUrl);
        TeachplanMedia save = teachplanMediaRepository.save(one);
        log.info(save.toString());
    }
}
