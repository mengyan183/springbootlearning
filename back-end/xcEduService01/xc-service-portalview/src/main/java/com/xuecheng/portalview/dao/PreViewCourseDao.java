package com.xuecheng.portalview.dao;

import com.xuecheng.framework.domain.portalview.PreViewCourse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PreViewCourseDao extends MongoRepository<PreViewCourse,String> {

}
