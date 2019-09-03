/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.TeachplanService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * CourseController
 *
 * @author guoxing
 * @date 9/3/2019 4:37 PM
 * @since 2.0.0
 **/
@Api(value = "课程管理")
@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {
    @Autowired
    private TeachplanService teachplanService;
    /**
     * 根据课程id获取课程计划
     *
     * @param courseId
     * @author guoxing
     * @date 2019-09-03 4:34 PM
     * @since 2.0.0
     */
    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachPlanList(@PathVariable("courseId") String courseId) {
        return teachplanService.findTeachPlanListByTopPlan(courseId);
    }

    /**
     * 新增课程计划
     *
     * @param teachplan
     * @author guoxing
     * @date 2019-09-03 6:07 PM
     * @since 2.0.0
     */
    @Override
    @PostMapping("/teachplan/add")
    public ResponseResult addTeachPlan(@RequestBody Teachplan teachplan) {
        return teachplanService.addTeachPlan(teachplan);
    }
}
