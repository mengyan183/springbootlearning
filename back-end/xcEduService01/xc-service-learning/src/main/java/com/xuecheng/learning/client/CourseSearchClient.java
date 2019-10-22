package com.xuecheng.learning.client;

import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * CourseSearchClient
 *
 * @author guoxing
 * @date 10/22/2019 10:12 AM
 * @since 2.0.0
 **/
@FeignClient("xc-search-service")
public interface CourseSearchClient {

    /**
     * 远程调用 课程计划媒资信息
     *
     * @param teachplanId
     * @return
     */
    @GetMapping(value = "/getmedia/{teachplanId}")
    TeachplanMediaPub getmedia(@PathVariable("teachplanId") String teachplanId);
}
