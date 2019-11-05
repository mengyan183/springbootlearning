package com.xuecheng.portalview.web.controller;

import com.xuecheng.api.portalview.ViewCourseControllerApi;
import com.xuecheng.framework.domain.portalview.PreViewCourse;
import com.xuecheng.framework.domain.portalview.PreViewCourseMedia;
import com.xuecheng.framework.domain.portalview.ViewCourse;
import com.xuecheng.framework.domain.portalview.ViewCourseMedia;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.portalview.service.ViewCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: mrt.
 * @Description:
 * @Date:Created in 2018/1/24 11:17.
 * @Modified By:
 */
@RestController
public class ViewCourseController implements ViewCourseControllerApi{

    @Autowired
    ViewCourseService viewCourseService;

    @Override
    public ResponseResult add(@PathVariable("courseId") String courseId,@RequestBody PreViewCourse preViewCourse) {
        //将课程视图存储到预览视图中
        PreViewCourse add = viewCourseService.add(courseId,preViewCourse);
        if(add!=null){
            ResponseResult responseResult = new ResponseResult(CommonCode.SUCCESS);
            return responseResult;
        }
        ResponseResult responseResult = new ResponseResult(CommonCode.FAIL);
        return responseResult;
    }

    @Override
    public PreViewCourse findpreById(@PathVariable("id") String id) {
        PreViewCourse preViewCourse = viewCourseService.findPreCourseById(id);
        return preViewCourse;
    }

    @Override
    public ViewCourse findById(@PathVariable("id") String id) {
        ViewCourse viewCourse = viewCourseService.findCourseById(id);
        return viewCourse;
    }

    @Override
    public ResponseResult publish(@PathVariable("courseId") String courseId) {
        ResponseResult publish = viewCourseService.publish(courseId);
        return publish;
    }

    @Override
    public ResponseResult addmedia(@PathVariable("courseId") String courseId, @RequestBody List<PreViewCourseMedia> preViewCourseMedias) {
        ResponseResult responseResult = viewCourseService.addmedia(courseId, preViewCourseMedias);
        return responseResult;
    }

    @Override
    public ViewCourseMedia findMediaById(@PathVariable("id") String id) {
        ViewCourseMedia viewCourseMedia = viewCourseService.findMediaById(id);
        return viewCourseMedia;
    }

}
