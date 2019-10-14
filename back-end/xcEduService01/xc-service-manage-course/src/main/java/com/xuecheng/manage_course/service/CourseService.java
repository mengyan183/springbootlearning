/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.manage_course.dao.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
