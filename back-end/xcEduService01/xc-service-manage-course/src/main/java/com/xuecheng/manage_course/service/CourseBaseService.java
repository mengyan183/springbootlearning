package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * CourseBaseService
 * 课程基础信息
 * @author guoxing
 * @date 10/14/2019 4:28 PM
 * @since 2.0.0
 **/
@Service
public class CourseBaseService {
    @Autowired
    private CourseBaseRepository courseBaseRepository;

    /**
     * 添加课程
     *
     * @author guoxing
     * @date 2019-10-14 4:29 PM
     * @since 2.0.0
     **/
    @Transactional // 添加事务
    public AddCourseResult addCourseBase(CourseBase courseBase) {
        Objects.requireNonNull(courseBase,"数据不能为空");
        // 课程名称
        String name = courseBase.getName();
        if(StringUtils.isBlank(name)){
            return new AddCourseResult(CommonCode.INVALID_PARAM,courseBase.getId());
        }
        // 课程等级
        String grade = courseBase.getGrade();
        if(StringUtils.isBlank(grade)){
            return new AddCourseResult(CommonCode.INVALID_PARAM,courseBase.getId());
        }
        // 学习模式
        String studymodel = courseBase.getStudymodel();
        if(StringUtils.isBlank(studymodel)){
            return new AddCourseResult(CommonCode.INVALID_PARAM,courseBase.getId());
        }
        //课程状态默认为未发布
        courseBase.setStatus("202001");
        courseBaseRepository.save(courseBase);
        return new AddCourseResult(CommonCode.SUCCESS,courseBase.getId());
    }

}
