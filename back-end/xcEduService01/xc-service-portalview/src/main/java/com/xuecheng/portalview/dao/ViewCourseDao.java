package com.xuecheng.portalview.dao;

import com.xuecheng.framework.domain.portalview.ViewCourse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ViewCourseDao extends MongoRepository<ViewCourse,String> {

}
