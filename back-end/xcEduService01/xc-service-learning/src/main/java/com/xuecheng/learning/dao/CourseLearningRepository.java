package com.xuecheng.learning.dao;

import com.xuecheng.framework.domain.learning.XcLearningCourse;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CourseLearningRepository
 *
 * @author guoxing
 * @date 10/22/2019 10:01 AM
 * @since 2.0.0
 **/
public interface CourseLearningRepository extends JpaRepository<XcLearningCourse, String> {
}
