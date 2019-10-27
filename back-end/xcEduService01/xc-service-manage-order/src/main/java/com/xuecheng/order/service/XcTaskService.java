package com.xuecheng.order.service;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.order.dao.XcTaskHisRepository;
import com.xuecheng.order.dao.XcTaskRepository;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.DateUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class XcTaskService {
    @Autowired
    private XcTaskRepository xcTaskRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private XcTaskHisRepository xcTaskHisRepository;

    /**
     * 分页获取 指定时间之前的所有任务
     *
     * @param updateTime
     * @param pageSize
     * @return
     */
    public List<XcTask> listPageXcTaskByUpdateTime(Date updateTime, Integer pageSize) {
        pageSize = pageSize == null || pageSize < 1 ? 10 : pageSize;
        updateTime = updateTime == null ? DateUtil.now() : updateTime;
        Page<XcTask> byUpdateTimeBefore = xcTaskRepository.findByUpdateTimeBefore(PageRequest.of(0, pageSize), updateTime);
        return byUpdateTimeBefore.getContent();
    }

    /**
     * 发布消息
     * 1:发送任务到mq
     */
    @Transactional
    public void publish(XcTask xcTask, String exchange, String routingKey) {
        if (xcTask == null || StringUtils.isBlank(exchange) || StringUtils.isBlank(routingKey)) {
            return;
        }
        String id = xcTask.getId();
        if (StringUtils.isBlank(id)) {
            return;
        }
        Optional<XcTask> byId = xcTaskRepository.findById(id);
        if (byId.isPresent()) {
            // 发送mq消息
            rabbitTemplate.convertAndSend(exchange, routingKey, byId.get());
            // 更新任务更新时间
            xcTaskRepository.updateTaskTime(id);
        }
    }

    /**
     * 完成 选课
     * 1:删除任务数据
     * 2:完成任务表中添加数据
     *
     * @param xcTask
     */
    @Transactional
    public void finishChooseAddCourse(XcTask xcTask) {
        if (xcTask == null) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        String id = xcTask.getId();
        if (StringUtils.isBlank(id)) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        Optional<XcTask> byId = xcTaskRepository.findById(id);
        if (byId.isPresent()) {
            xcTask = byId.get();
            xcTaskRepository.delete(xcTask);
            XcTaskHis xcTaskHis = new XcTaskHis();
            BeanUtils.copyProperties(xcTask, xcTaskHis);
            xcTaskHisRepository.save(xcTaskHis);
        }
    }
}
