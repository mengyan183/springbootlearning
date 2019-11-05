package com.xuecheng.portalview.dao;

import com.xuecheng.framework.domain.portalview.PreViewCourseMedia;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PreViewCourseMediaDao extends MongoRepository<PreViewCourseMedia,String> {
    //根据课程id查询课程媒资视图
    List<PreViewCourseMedia> findByCourseId(String courseId);
}
