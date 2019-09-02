/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.test.rabbitmq.consumer.mq;

import com.rabbitmq.client.Channel;
import com.xuecheng.test.rabbitmq.consumer.config.RabbitmqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * ConsumerHandler
 *
 * @author guoxing
 * @date 9/2/2019 3:35 PM
 * @since 2.0.0
 **/
@Component
@Slf4j
public class ConsumerHandler {

    /**
     * 使用 RabbitListener 监听队列
     *
     * @param msg
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_EMAIL})
    public void sendEmail(String msg, Message message, Channel channel) {
        log.info(msg);
    }

    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_SMS})
    public void sendSms(String msg, Message message, Channel channel) {
        log.info(msg);
    }

}
