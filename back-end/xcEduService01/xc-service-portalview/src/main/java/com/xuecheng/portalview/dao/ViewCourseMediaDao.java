package com.xuecheng.portalview.dao;

import com.xuecheng.framework.domain.portalview.ViewCourseMedia;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ViewCourseMediaDao extends MongoRepository<ViewCourseMedia,String> {

}
