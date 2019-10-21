/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.manage_course.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
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
import com.xuecheng.manage_course.dao.CoursePubRepository;
import com.xuecheng.manage_course.dao.TeachplanMediaRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
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
@Slf4j
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
    @Autowired
    private CoursePubRepository coursePubRepository;

    @Autowired
    private TeachplanMediaRepository teachplanMediaRepository;

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
        if (StringUtils.isBlank(courseId)) {
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

    /**
     * 课程发布
     * 1：调用cms课程发布接口
     *
     * @param courseId
     * @return
     */
    @Transactional
    public CoursePublishResult publish(String courseId) {
        if (StringUtils.isBlank(courseId)) {
            return new CoursePublishResult(CommonCode.FAIL, null);
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
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        if (cmsPostPageResult != null && cmsPostPageResult.isSuccess()) {
            // 更改课程状态
            courseBase.setStatus("202002");
            ResponseResult responseResult = courseBaseService.updateCourseBase(courseId, courseBase);
            if (responseResult == null || !responseResult.isSuccess()) {
                ExceptionCast.cast(CommonCode.FAIL);
            }

            //搜索相关
            // 将课程相关数据 保存到coursepub中
            CoursePub coursePub = new CoursePub();
            // course base
            BeanUtils.copyProperties(courseBase, coursePub);
            // course pic
            CoursePic coursePic = this.getCoursePic(courseId);
            BeanUtils.copyProperties(coursePic, coursePub);
            // course market
            CourseMarket courseMarketById = courseMarketService.getCourseMarketById(courseId);
            BeanUtils.copyProperties(courseMarketById, coursePub);
            // course plan
            TeachplanNode teachPlanListByTopPlan = teachplanService.findTeachPlanListByTopPlan(courseId);
            coursePub.setTeachplan(JSON.toJSONString(teachPlanListByTopPlan));

            if (StringUtils.isNotEmpty(courseId)) {
                ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
            }
            CoursePub coursePubNew = null;
            Optional<CoursePub> coursePubOptional = coursePubRepository.findById(courseId);
            if (coursePubOptional.isPresent()) {
                coursePubNew = coursePubOptional.get();
            }
            if (coursePubNew == null) {
                coursePubNew = new CoursePub();
            }
            BeanUtils.copyProperties(coursePub, coursePubNew); //设置主键
            coursePubNew.setId(courseId);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY‐MM‐dd HH:mm:ss");
            //更新时间戳为最新时间
            coursePub.setTimestamp(new Date());
            String date = simpleDateFormat.format(new Date());
            coursePub.setPubTime(date);
            //保存到数据库
            coursePubRepository.save(coursePub);
            // ... 还有剩余步骤

            return new CoursePublishResult(CommonCode.SUCCESS, cmsPostPageResult.getPageUrl());
        } else {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        return null;
    }

    /**
     * 保存课程计划 和 媒资数据 关联关系
     *
     * @param teachplanMedia
     * @return
     */
    public ResponseResult savemedia(TeachplanMedia teachplanMedia) {
        if (teachplanMedia == null || StringUtils.isBlank(teachplanMedia.getTeachplanId())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        // 1: 判断课程计划层级是否为3
        String teachplanId = teachplanMedia.getTeachplanId();
        Teachplan teachplan = teachplanService.findById(teachplanId);
        if (teachplan == null) {
            log.error("未找到该课程计划:{}", teachplanId);
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        String grade = teachplan.getGrade();
        if (!"3".equals(grade)) {
            log.error("三级课程计划才可以关联媒资");
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        Optional<TeachplanMedia> teachplanMediaOptional = teachplanMediaRepository.findById(teachplanId);
        // 如果查询不存在,创建新的对象
        TeachplanMedia one = teachplanMediaOptional.orElseGet(TeachplanMedia::new);
        //保存媒资信息与课程计划信息
        one.setTeachplanId(teachplanId);
        one.setCourseId(teachplanMedia.getCourseId());
        one.setMediaFileOriginalName(teachplanMedia.getMediaFileOriginalName());
        one.setMediaId(teachplanMedia.getMediaId());
        one.setMediaUrl(teachplanMedia.getMediaUrl());
        teachplanMediaRepository.save(one);
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
