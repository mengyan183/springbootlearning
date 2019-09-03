/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * CourseService
 *
 * @author guoxing
 * @date 9/3/2019 4:40 PM
 * @since 2.0.0
 **/
@Service
public class TeachplanService {

    @Autowired
    private TeachplanMapper teachplanMapper;

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

}
