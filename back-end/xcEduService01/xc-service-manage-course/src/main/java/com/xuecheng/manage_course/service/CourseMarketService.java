package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseMarketRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CourseMarketService
 * 课程营销信息
 *
 * @author guoxing
 * @date 10/14/2019 5:36 PM
 * @since 2.0.0
 **/
@Service
public class CourseMarketService {

    @Autowired
    private CourseMarketRepository courseMarketRepository;

    /**
     * 获取课程营销信息
     *
     * @author guoxing
     * @date 2019-10-14 5:37 PM
     * @since 2.0.0
     **/
    public CourseMarket getCourseMarketById(String courseId) {
        if(StringUtils.isBlank(courseId)){
            return null;
        }
        return courseMarketRepository.findById(courseId).orElse(null);
    }

    /**
     * 编辑课程营销信息
     *
     * @author guoxing
     * @date 2019-10-14 5:37 PM
     * @since 2.0.0
     **/
    public CourseMarket editCourseMarket(String courseId, CourseMarket courseMarket) {
        if(StringUtils.isBlank(courseId) || courseMarket == null){
            return null;
        }
        CourseMarket one = this.getCourseMarketById(courseId);
        if(one!=null){
            one.setCharge(courseMarket.getCharge());
            one.setStartTime(courseMarket.getStartTime());//课程有效期，开始时间
            one.setEndTime(courseMarket.getEndTime());//课程有效期，结束时间
            one.setPrice(courseMarket.getPrice());
            one.setQq(courseMarket.getQq());
            one.setValid(courseMarket.getValid());
            courseMarketRepository.save(one);
        }else{
            //添加课程营销信息
            one = new CourseMarket();
            BeanUtils.copyProperties(courseMarket, one);
        //设置课程id
            one.setId(courseId);
            courseMarketRepository.save(one);
        }
        return one;
    }
}
