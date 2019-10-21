package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.TeachplanMedia;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * TeachplanMediaRepository
 *
 * @author guoxing
 * @date 10/21/2019 4:16 PM
 * @since 2.0.0
 **/
public interface TeachplanMediaRepository extends MongoRepository<TeachplanMedia, String> {
}
