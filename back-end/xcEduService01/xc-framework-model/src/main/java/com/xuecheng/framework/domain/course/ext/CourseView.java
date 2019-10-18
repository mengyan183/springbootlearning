package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * CourseView
 * 课程展示 数据
 *
 * @author guoxing
 * @date 10/18/2019 11:05 AM
 * @since 2.0.0
 **/
@Data
@ToString
@NoArgsConstructor
public class CourseView implements Serializable {
    private static final long serialVersionUID = -4507901012347205386L;
    private CourseBase courseBase;//基础信息
    private CourseMarket courseMarket;//课程营销
    private CoursePic coursePic;//课程图片
    private TeachplanNode teachplanNode;//教学计划
}
