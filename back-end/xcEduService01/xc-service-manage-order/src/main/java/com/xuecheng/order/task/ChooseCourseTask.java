package com.xuecheng.order.task;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.order.service.XcTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.assertj.core.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 选课定时任务
 */
@Component
@Slf4j
public class ChooseCourseTask {

    @Autowired
    private XcTaskService xcTaskService;

    /**
     * 每隔1分钟获取一分钟前的任务
     */
    @Scheduled(fixedDelay = 60000)
    public void listTask() {
        List<XcTask> xcTasks = xcTaskService.listPageXcTaskByUpdateTime(DateUtils.addMinutes(DateUtil.now(), -1), 10);
        if (CollectionUtils.isEmpty(xcTasks)) {
            return;
        }
        for (XcTask xcTask : xcTasks) {
            if (xcTask != null) {
                xcTaskService.publish(xcTask, xcTask.getMqExchange(), xcTask.getMqRoutingkey());
                log.info("发送任务消息:{}", xcTask);
            }
        }
    }
}
