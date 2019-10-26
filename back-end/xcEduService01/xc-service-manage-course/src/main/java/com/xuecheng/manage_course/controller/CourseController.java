/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.manage_course.controller;

import com.github.pagehelper.Page;
import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.XcOauth2Util;
import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_course.service.CourseBaseService;
import com.xuecheng.manage_course.service.CourseMarketService;
import com.xuecheng.manage_course.service.CourseService;
import com.xuecheng.manage_course.service.TeachplanService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class CourseController extends BaseController implements CourseControllerApi {
    @Autowired
    private TeachplanService teachplanService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseBaseService courseBaseService;
    @Autowired
    private CourseMarketService courseMarketService;

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

    @Override
    @GetMapping("/coursebase/list/{page}/{size}")
    public QueryResponseResult<CourseInfo> findCourseList(@PathVariable("page") int page, @PathVariable("size") int size, CourseListRequest courseListRequest) {

        if (courseListRequest == null) {
            courseListRequest = new CourseListRequest();
        }
        // 解析header 中 authorization 包含的jwt令牌,转换为解密数据(用户信息)
        XcOauth2Util xcOauth2Util = new XcOauth2Util();
        XcOauth2Util.UserJwt userJwtFromHeader = xcOauth2Util.getUserJwtFromHeader(request);
        if (userJwtFromHeader != null) {
            String companyId = userJwtFromHeader.getCompanyId();
            courseListRequest.setCompanyId(companyId);
        }

        //分页查询
        Page<CourseInfo> list = courseService.list(page, size, courseListRequest);
        QueryResult<CourseInfo> queryResult = new QueryResult<>();
        queryResult.setList(list.getResult());
        queryResult.setTotal(list.getTotal());
        return new QueryResponseResult<>(CourseCode.COURSE_SUCCESS, queryResult);
    }

    @Override
    @PostMapping("/coursebase/add")
    public AddCourseResult addCourseBase(@RequestBody CourseBase courseBase) {
        return courseBaseService.addCourseBase(courseBase);
    }

    @Override
    @GetMapping("/coursebase/get/{courseBaseId}")
    public CourseBase getCoursebaseById(@PathVariable("courseBaseId") String courseBaseId) {
        return courseBaseService.getCoursebaseById(courseBaseId);
    }

    @Override
    @PostMapping("/coursebase/update/{courseBaseId}")
    public ResponseResult updateCourseBase(@PathVariable("courseBaseId") String courseBaseId, @RequestBody CourseBase courseBase) {
        return courseBaseService.updateCourseBase(courseBaseId, courseBase);
    }

    @Override
    @GetMapping("/coursemarket/get/{courseId}")
    public CourseMarket getCourseMarketById(@PathVariable("courseId") String courseId) {
        return courseMarketService.getCourseMarketById(courseId);
    }

    @Override
    @PostMapping("/coursemarket/edit/{courseId}")
    public ResponseResult editCourseMarket(@PathVariable("courseId") String courseId, @RequestBody CourseMarket courseMarket) {
        return courseMarketService.editCourseMarket(courseId, courseMarket);
    }

    @Override
    @PostMapping("/coursepic/add")
    public ResponseResult addCoursePic(@RequestParam("courseId") String courseId,
                                       @RequestParam("pic") String pic) {
        //保存课程图片
        return courseService.saveCoursePic(courseId, pic);
    }

    @Override
    @GetMapping("/coursepic/get/{courseId}")
    @PreAuthorize("hasAuthority('coursepic_get')") // 使用spring authority 相关注解表示该方法要求符合权限才能访问该方法
    public CoursePic getCoursePic(@PathVariable("courseId") String courseId) {
        return courseService.getCoursePic(courseId);
    }

    @Override
    @DeleteMapping("/coursepic/delete")
    public ResponseResult deleteCoursePic(@RequestParam("courseId") String courseId) {
        return courseService.deleteCoursePic(courseId);
    }

    @Override
    @GetMapping("/courseview/{id}")
    public CourseView courseview(@PathVariable("id") String id) {
        return courseService.getCourseView(id);
    }

    @Override
    @PostMapping("/preview/{id}")
    public CoursePublishResult preview(@PathVariable("id") String id) {
        return courseService.preview(id);
    }

    @Override
    @PostMapping("/publish/{id}")
    public CoursePublishResult publish(@PathVariable("id") String courseId) {
        return courseService.publish(courseId);
    }

    @Override
    @PostMapping("/savemedia")
    public ResponseResult savemedia(@RequestBody TeachplanMedia teachplanMedia) {
        return courseService.savemedia(teachplanMedia);
    }
}
