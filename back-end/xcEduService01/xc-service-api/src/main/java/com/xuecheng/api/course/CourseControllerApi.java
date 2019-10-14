/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * CourseControllerApi
 *
 * @author guoxing
 * @date 9/3/2019 4:03 PM
 * @since 2.0.0
 **/
@Api(value = "课程管理接口")
public interface CourseControllerApi {
    /**
     * 根据课程id获取课程计划
     *
     * @author guoxing
     * @date 2019-09-03 4:34 PM
     * @since 2.0.0
     **/
    @ApiOperation("根据课程id获取课程计划")
    TeachplanNode findTeachPlanList(String courseId);

    /**
     * 新增课程计划
     *
     * @author guoxing
     * @date 2019-09-03 6:07 PM
     * @since 2.0.0
     **/
    @ApiOperation("新增课程计划")
    ResponseResult addTeachPlan(Teachplan teachplan);

    //查询课程列表
    @ApiOperation("查询我的课程列表")
    QueryResponseResult<CourseInfo> findCourseList(int page, int size, CourseListRequest courseListRequest);
}
