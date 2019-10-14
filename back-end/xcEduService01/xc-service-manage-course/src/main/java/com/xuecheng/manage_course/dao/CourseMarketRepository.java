package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseMarket;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CourseMarketRepository
 *
 * @author guoxing
 * @date 10/14/2019 5:40 PM
 * @since 2.0.0
 **/
public interface CourseMarketRepository extends JpaRepository<CourseMarket,String> {
}
