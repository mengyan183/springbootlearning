/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.config.SystemConfig;
import com.xuecheng.manage_course.dao.CourseMapper;
import com.xuecheng.manage_course.dao.CoursePicRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
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
    @Autowired
    private CourseBaseService courseBaseService;
    @Autowired
    private CourseMarketService courseMarketService;
    @Autowired
    private TeachplanService teachplanService;

    @Autowired
    private CmsPageClient cmsPageClient;
    @Autowired
    private SystemConfig systemConfig;

    @Value("${xuecheng.imagehost}")
    private String imageHost;

    public Page<CourseInfo> list(Integer page, Integer size, CourseListRequest courseListRequest) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null || size < 1) {
            size = 10;
        }
        // page helper 拦截 下一个 sql 拼接 limit 条件 , 并执行count查询(可以根据配置文件 开关状态 )
        PageHelper.startPage(page, size);
        Page<CourseInfo> list = courseMapper.findList(courseListRequest);
        if (list != null) {
            List<CourseInfo> result = list.getResult();
            if (!CollectionUtils.isEmpty(result)) {
                for (CourseInfo courseInfo : result) {
                    if (courseInfo != null && StringUtils.isNotBlank(courseInfo.getPic())) {
                        courseInfo.setPic(imageHost + "/" + courseInfo.getPic());
                    }
                }
            }
        }
        return list;
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
        CoursePic coursePic = coursePicRepository.findById(courseId).orElse(null);
        if (coursePic != null) {
            coursePic.setPic(imageHost + "/" + coursePic.getPic());
        }
        return coursePic;
    }

    /**
     * 删除课程图片
     * 1: 删除课程和图片的关联关系
     * 2: 删除 fastdfs中文件
     *
     * @param courseId
     * @return
     */
    @Transactional
    public ResponseResult deleteCoursePic(String courseId) {
        if (StringUtils.isBlank(courseId)) {
            return new ResponseResult(CommonCode.INVALID_PARAM);
        }
        long i = coursePicRepository.deleteByCourseid(courseId);
        if (i > 0) {
            //TODO : 删除 fastdfs 文件

            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    /**
     * 获取 课程展示数据
     *
     * @author guoxing
     * @date 2019-10-18 11:09 AM
     * @since 2.0.0
     **/
    public CourseView getCourseView(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        CourseView courseView = new CourseView();
        // 课程基础信息
        courseView.setCourseBase(courseBaseService.getCoursebaseById(id));
        // 课程营销信息
        courseView.setCourseMarket(courseMarketService.getCourseMarketById(id));
        // 课程图片信息
        courseView.setCoursePic(this.getCoursePic(id));
        // 教学计划节点
        courseView.setTeachplanNode(teachplanService.findTeachPlanListByTopPlan(id));
        return courseView;
    }

    /**
     * 课程预览
     *
     * @author guoxing
     * @date 2019-10-18 3:34 PM
     * @since 2.0.0
     **/
    public CoursePublishResult preview(String courseId) {
        if (StringUtils.isBlank(courseId)) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        CourseBase courseBase = courseBaseService.getCoursebaseById(courseId);
        if (courseBase == null) {
            ExceptionCast.cast(CourseCode.COURSE_NOT_EXIST);
        }
        // 请求 cms 添加页面
        CmsPage cmsPage = new CmsPage();
        //站点 课程预览站点
        cmsPage.setSiteId(systemConfig.getPublish_siteId());
        //模板
        cmsPage.setTemplateId(systemConfig.getPublish_templateId());
        //页面名称
        cmsPage.setPageName(courseId + ".html");
        //页面别名
        cmsPage.setPageAliase(courseBase.getName());
        //页面访问路径
        cmsPage.setPageWebPath(systemConfig.getPublish_page_webpath());
        //页面存储路径
        cmsPage.setPagePhysicalPath(systemConfig.getPublish_page_physicalpath());
        //数据url
        cmsPage.setDataUrl(systemConfig.getPublish_dataUrlPre() + courseId);
        CmsPageResult cmsPageResult = cmsPageClient.save(cmsPage);
        if (cmsPageResult == null || !cmsPageResult.isSuccess()) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        // 拼装页面预览URL
        cmsPage = cmsPageResult.getCmsPage();
        String pageId = cmsPage.getPageId();
        // 返回 对象 (包含 页面 预览 url地址  )
        return new CoursePublishResult(CommonCode.SUCCESS, systemConfig.getPreviewUrl() + pageId);
    }
}
