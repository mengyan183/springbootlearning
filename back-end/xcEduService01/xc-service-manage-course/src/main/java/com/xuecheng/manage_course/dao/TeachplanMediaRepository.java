package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.TeachplanMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * TeachplanMediaRepository
 *
 * @author guoxing
 * @date 10/21/2019 4:16 PM
 * @since 2.0.0
 **/
public interface TeachplanMediaRepository extends JpaRepository<TeachplanMedia, String> {
    /**
     * 根据课程id获取教学计划媒资信息
     *
     * @param courseId
     * @return
     */
    List<TeachplanMedia> findByCourseId(String courseId);
}
