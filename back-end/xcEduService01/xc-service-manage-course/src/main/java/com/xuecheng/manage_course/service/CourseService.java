/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseMapper;
import com.xuecheng.manage_course.dao.CoursePicRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * CourseService
 *
 * @author guoxing
 * @date 9/3/2019 4:40 PM
 * @since 2.0.0
 **/
@Service
public class CourseService {
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CoursePicRepository coursePicRepository;


    public Page<CourseInfo> list(Integer page, Integer size, CourseListRequest courseListRequest) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null || size < 1) {
            size = 10;
        }
        // page helper 拦截 下一个 sql 拼接 limit 条件 , 并执行count查询(可以根据配置文件 开关状态 )
        PageHelper.startPage(page, size);
        return courseMapper.findList(courseListRequest);
    }

    //添加课程图片
    @Transactional
    public ResponseResult saveCoursePic(String courseId, String pic) {
        //查询课程图片
        Optional<CoursePic> picOptional = coursePicRepository.findById(courseId);
        CoursePic coursePic = null;
        if (picOptional.isPresent()) {
            coursePic = picOptional.get();
        }
        //没有课程图片则新建对象
        if (coursePic == null) {
            coursePic = new CoursePic();
        }
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        //保存课程图片
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public CoursePic getCoursePic(String courseId) {
        if(StringUtils.isBlank(courseId)){
            return null;
        }
        CoursePic coursePic = new CoursePic();
        coursePic.setCourseid(courseId);
        //TODO 
        return coursePicRepository.findOne(coursePic);
    }
}
