package com.xuecheng.order.mq;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.order.config.RabbitMQConfig;
import com.xuecheng.order.service.XcTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 选课任务完成消息监听
 */
@Component
@Slf4j
public class FinishChooseCourseTaskListener {
    @Autowired
    private XcTaskService xcTaskService;

    /**
     * 1:接收到选课完成的任务消息,删除当前任务,保存当前任务到任务完成表中
     *
     * @param xcTask
     * @param message
     */
    @RabbitListener(queues = {RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE})
    public void addCourse(XcTask xcTask, Message message) {
        if (xcTask != null) {
            xcTaskService.finishChooseAddCourse(xcTask);
            log.info("完成选课;{}", xcTask);
        }
    }
}
