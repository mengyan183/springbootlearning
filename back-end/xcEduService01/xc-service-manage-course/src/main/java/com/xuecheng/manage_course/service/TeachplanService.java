/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import com.xuecheng.manage_course.dao.TeachplanRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
public class TeachplanService {
    private static Logger LOGGER = LoggerFactory.getLogger(TeachplanService.class);

    @Autowired
    private TeachplanMapper teachplanMapper;
    @Autowired
    private CourseBaseRepository courseBaseRepository;
    @Autowired
    private TeachplanRepository teachplanRepository;

    /**
     * 根据课程id获取课程计划及其子计划 ;
     *
     * @author guoxing
     * @date 2019-09-03 4:49 PM
     * @since 2.0.0
     **/
    public TeachplanNode findTeachPlanListByTopPlan(String courseId) {
        Teachplan teachplan = teachplanMapper.getMainTeachplanByCourseId(courseId);
        if (teachplan == null) {
            return null;
        }
        TeachplanNode teachplanNode = new TeachplanNode();
        getById(teachplan, teachplanNode);
        return teachplanNode;
    }

    /**
     * 递归获取该节点下的所有子节点
     *
     * @author guoxing
     * @date 2019-09-03 5:24 PM
     * @since 2.0.0
     **/
    private void getById(Teachplan teachplan, TeachplanNode teachplanNode) {
        if (teachplan == null || teachplanNode == null) {
            return;
        }
        BeanUtils.copyProperties(teachplan, teachplanNode);
        // 获取 其下面的所有子节点
        List<Teachplan> teachplans = teachplanMapper.listByParentId(teachplan.getId());
        if (!CollectionUtils.isEmpty(teachplans)) {
            ArrayList<TeachplanNode> teachplanNodes = new ArrayList<>(teachplans.size());
            for (Teachplan existTeachPlan : teachplans) {
                if (existTeachPlan != null) {
                    TeachplanNode nextTeachplanNode = new TeachplanNode();
                    // 递归获取下一个节点
                    getById(existTeachPlan, nextTeachplanNode);
                    teachplanNodes.add(nextTeachplanNode);
                }
            }
            teachplanNode.setChildren(teachplanNodes);
        } else {
            // 如果子节点为空 则停止递归
            teachplanNode.setChildren(new ArrayList<>(0));
        }
    }

    /**
     * 添加课程计划
     *
     * @author guoxing
     * @date 2019-09-03 6:09 PM
     * @since 2.0.0
     **/
    @Transactional
    public ResponseResult addTeachPlan(Teachplan teachplan) {
        if (teachplan == null || StringUtils.isBlank(teachplan.getCourseid()) || StringUtils.isBlank(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        String courseid = teachplan.getCourseid();
        String parentid = teachplan.getParentid();
        if (StringUtils.isBlank(parentid)) {
            // 获取根节点
            Teachplan mainTeachplanByCourseId = teachplanMapper.getMainTeachplanByCourseId(courseid);
            if (mainTeachplanByCourseId == null) {
                // 获取课程信息
                Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(courseid);
                if (!courseBaseOptional.isPresent()) {
                    LOGGER.error("未找到课程信息,courseId:{}", courseid);
                    ExceptionCast.cast(CommonCode.INVALID_PARAM);
                }
                CourseBase courseBase = courseBaseOptional.get();
                // 生成新的根节点
                //新增一个根结点
                Teachplan teachplanRoot = new Teachplan();
                teachplanRoot.setCourseid(courseid);
                teachplanRoot.setPname(courseBase.getName());
                teachplanRoot.setParentid("0");
                teachplanRoot.setGrade("1");//1级
                teachplanRoot.setStatus("0");//未发布
                teachplanRepository.save(teachplanRoot);
                teachplan.setParentid(teachplanRoot.getId());
            } else {
                teachplan.setParentid(mainTeachplanByCourseId.getId());
            }
            teachplan.setGrade("2");
        } else {
            teachplan.setGrade("3");
        }
        teachplanRepository.save(teachplan);
        return ResponseResult.SUCCESS();
    }
}
