package com.xuecheng.learning.service;

import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.learning.response.GetMediaResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.learning.client.CourseSearchClient;
import com.xuecheng.learning.dao.CourseLearningRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CourseLearningService
 *
 * @author guoxing
 * @date 10/22/2019 10:00 AM
 * @since 2.0.0
 **/
@Service
@Slf4j
public class CourseLearningService {
    @Autowired
    private CourseLearningRepository courseLearningRepository;
    @Autowired
    private CourseSearchClient courseSearchClient;

    /**
     * 获取课程视频播放地址
     *
     * @param courseId
     * @param teachplanId
     * @return
     */
    public GetMediaResult getMedia(String courseId, String teachplanId) {
        if (StringUtils.isBlank(courseId) || StringUtils.isBlank(teachplanId)) {
            return new GetMediaResult(CommonCode.INVALID_PARAM, null);
        }
        //TODO :校验学生学习权限

        // 获取课程计划播放地址
        TeachplanMediaPub getmedia = courseSearchClient.getmedia(teachplanId);
        if (getmedia == null || StringUtils.isBlank(getmedia.getMediaUrl())) {
            log.error("远程获取课程媒资信息 失败;{}", getmedia);
            return new GetMediaResult(CommonCode.FAIL, null);
        }
        return new GetMediaResult(CommonCode.SUCCESS, getmedia.getMediaUrl());
    }
}
