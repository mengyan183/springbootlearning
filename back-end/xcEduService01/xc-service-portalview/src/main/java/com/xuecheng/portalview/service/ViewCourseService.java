package com.xuecheng.portalview.service;

import com.xuecheng.framework.domain.portalview.PreViewCourse;
import com.xuecheng.framework.domain.portalview.PreViewCourseMedia;
import com.xuecheng.framework.domain.portalview.ViewCourse;
import com.xuecheng.framework.domain.portalview.ViewCourseMedia;
import com.xuecheng.framework.domain.portalview.response.PortalViewCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.portalview.dao.PreViewCourseDao;
import com.xuecheng.portalview.dao.PreViewCourseMediaDao;
import com.xuecheng.portalview.dao.ViewCourseDao;
import com.xuecheng.portalview.dao.ViewCourseMediaDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrt on 2018/2/27.
 */
@Service
public class ViewCourseService {
    @Autowired
    private ViewCourseDao viewCourseDao;
    @Autowired
    private PreViewCourseDao preViewCourseDao;
    @Autowired
    private PreViewCourseMediaDao preViewCourseMediaDao;
    @Autowired
    private ViewCourseMediaDao viewCourseMediaDao;

    //添加课程预览视图，根据课程id进行覆盖
    public PreViewCourse add(String courseId,PreViewCourse preViewCourse) {
        if(preViewCourse == null|| StringUtils.isEmpty(courseId)){
            ExceptionCast.cast(PortalViewCode.PortalView_ADDVIEWCOURSE_COURSEIDISNULL);
        }
        PreViewCourse save = preViewCourseDao.save(preViewCourse);
        return save;
    }


    //根据id查询课程预览视图
    public PreViewCourse findPreCourseById(String id) {
        return preViewCourseDao.findOne(id);

    }
    //根据id查询课程视图
    public ViewCourse findCourseById(String id) {
        return viewCourseDao.findOne(id);

    }

    //发布课程视图
    public ResponseResult publish(String courseId){
        //发布课程视图
        PreViewCourse preViewCourse = preViewCourseDao.findOne(courseId);
        if(preViewCourse == null){
            ExceptionCast.cast(PortalViewCode.PortalView_PUBLISH_PREVIEWCOURSE_ISNULL);
        }
        ViewCourse viewCourse = viewCourseDao.findOne(courseId);
        if(viewCourse==null){
            viewCourse = new ViewCourse();
        }
        BeanUtils.copyProperties(preViewCourse,viewCourse);
        viewCourseDao.save(viewCourse);
        //发布课程媒资视图
        List<PreViewCourseMedia> preViewCourseMediaList = preViewCourseMediaDao.findByCourseId(courseId);
        //取消课程媒资不上传不允许课程发布的限制
//        if(preViewCourseMediaList == null || preViewCourseMediaList.size()<=0){
//            ExceptionCast.cast(PortalViewCode.PortalView_PUBLISH_PREVIEWMEDIA_ISNULL);
//        }
        if(preViewCourseMediaList!=null){
            List<ViewCourseMedia> viewCourseMediaList = new ArrayList<>();
            for(PreViewCourseMedia preViewCourseMedia:preViewCourseMediaList){
                ViewCourseMedia viewCourseMedia = new ViewCourseMedia();
                BeanUtils.copyProperties(preViewCourseMedia,viewCourseMedia);
                viewCourseMediaList.add(viewCourseMedia);
            }
            viewCourseMediaDao.save(viewCourseMediaList);
        }

        return new ResponseResult(CommonCode.SUCCESS);
    }
    //添加课程媒资预览视图
    public ResponseResult addmedia(String courseId,List<PreViewCourseMedia> preViewCourseMedias) {
        if(preViewCourseMedias == null|| StringUtils.isEmpty(courseId)){
            ExceptionCast.cast(PortalViewCode.PortalView_ADDVIEWCOURSE_COURSEIDISNULL);
        }

        preViewCourseMediaDao.save(preViewCourseMedias);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //根据id查询课程媒资
    public ViewCourseMedia findMediaById(String id) {
        return viewCourseMediaDao.findOne(id);

    }
}
