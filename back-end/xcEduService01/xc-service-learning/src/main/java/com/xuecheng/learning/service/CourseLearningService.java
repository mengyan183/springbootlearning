package com.xuecheng.learning.service;

import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.learning.XcLearningCourse;
import com.xuecheng.framework.domain.learning.response.GetMediaResult;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.learning.client.CourseSearchClient;
import com.xuecheng.learning.config.RabbitMQConfig;
import com.xuecheng.learning.dao.CourseLearningRepository;
import com.xuecheng.learning.dao.XcTaskHisRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

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
    @Autowired
    private XcTaskHisRepository xcTaskHisRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

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


    /**
     * 添加课程(选课)
     * 1:从mq中接收到任务消息后,根据用户和课程id在已完成任务表中判断该用户是否已选择了该课程
     * 2: 如果已经选择该课程,则发送任务选课完成MQ消息; 如果未选择该课程,则添加课程,添加课程成功后,则发送任务选课完成MQ消息
     *
     * @param userId    用户id
     * @param courseId  课程id
     * @param valid
     * @param startTime
     * @param endTime
     * @param xcTask
     * @return
     */
    @Transactional
    public ResponseResult addCourse(String userId, String courseId, String valid, Date startTime, Date endTime, XcTask xcTask) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(courseId) || StringUtils.isBlank(valid) || startTime == null || endTime == null || xcTask == null) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        XcLearningCourse xcLearningCourse = courseLearningRepository.findXcLearningCourseByUserIdAndCourseId(userId, courseId);
        if (xcLearningCourse == null) {//没有选课记录则添加
            xcLearningCourse = new XcLearningCourse();
            xcLearningCourse.setUserId(userId);
            xcLearningCourse.setCourseId(courseId);
            xcLearningCourse.setValid(valid);
            xcLearningCourse.setStartTime(startTime);
            xcLearningCourse.setEndTime(endTime);
            xcLearningCourse.setStatus("501001");
            courseLearningRepository.save(xcLearningCourse);
        } else {//有选课记录则更新日期
            xcLearningCourse.setValid(valid);
            xcLearningCourse.setStartTime(startTime);
            xcLearningCourse.setEndTime(endTime);
            xcLearningCourse.setStatus("501001");
            courseLearningRepository.save(xcLearningCourse);
        } //向历史任务表播入记录
        String id = xcTask.getId();
        if (StringUtils.isBlank(id)) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        Optional<XcTaskHis> optional = xcTaskHisRepository.findById(id);
        if (!optional.isPresent()) {
            //添加历史任务
            XcTaskHis xcTaskHis = new XcTaskHis();
            BeanUtils.copyProperties(xcTask, xcTaskHis);
            xcTaskHisRepository.save(xcTaskHis);
        }
        // 发送选课成功消息到mq
        rabbitTemplate.convertAndSend(RabbitMQConfig.EX_LEARNING_ADDCHOOSECOURSE, RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE_KEY, xcTask);
        log.info("send finish choose course taskId:{}", id);
        return new ResponseResult();
    }
}
