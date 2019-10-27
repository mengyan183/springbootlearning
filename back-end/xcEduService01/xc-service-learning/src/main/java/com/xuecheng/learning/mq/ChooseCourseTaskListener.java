package com.xuecheng.learning.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.learning.config.RabbitMQConfig;
import com.xuecheng.learning.service.CourseLearningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 选课任务消息监听
 */
@Component
@Slf4j
public class ChooseCourseTaskListener {
    @Autowired
    private CourseLearningService courseLearningService;

    @RabbitListener(queues = {RabbitMQConfig.XC_LEARNING_ADDCHOOSECOURSE})
    public void addCourse(XcTask xcTask, Message message) {
        if (xcTask != null) {
            try {
                String requestBody = xcTask.getRequestBody();
                Map map = JSON.parseObject(requestBody, Map.class);
                String userId = (String) map.get("userId");
                String courseId = (String) map.get("courseId");
                String valid = (String) map.get("valid");
                Date startTime = null;
                Date endTime = null;
                SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY‐MM‐dd HH:mm:ss");
                if (map.get("startTime") != null) {
                    startTime = dateFormat.parse((String) map.get("startTime"));
                }
                if (map.get("endTime") != null) {
                    endTime = dateFormat.parse((String) map.get("endTime"));
                }
                //添加选课
                courseLearningService.addCourse(userId, courseId, valid, startTime, endTime, xcTask);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("选课失败");
            }
        }
    }
}
