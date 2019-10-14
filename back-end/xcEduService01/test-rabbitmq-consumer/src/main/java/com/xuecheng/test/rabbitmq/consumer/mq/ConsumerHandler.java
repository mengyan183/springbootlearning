/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.test.rabbitmq.consumer.mq;

import com.rabbitmq.client.Channel;
import com.xuecheng.test.rabbitmq.consumer.config.RabbitmqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
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


    @RabbitListener(queues = {RabbitmqConfig.IMMEDIATE_QUEUE_DELAY})
    public void receiveDelayMessage(String msg, Message message, Channel channel) {
        log.info(System.currentTimeMillis() + "");
        log.info(msg);
    }

    /**
     * 自定义 rabbitmq 监听器容器工厂
     *
     * @author guoxing
     * @date 2019-09-17 1:42 PM
     * @since 2.0.0
     **/
    @Bean
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory = new SimpleRabbitListenerContainerFactory();
//        simpleRabbitListenerContainerFactory.setTxSize();
//        simpleRabbitListenerContainerFactory.setConcurrentConsumers();
//        simpleRabbitListenerContainerFactory.setMaxConcurrentConsumers();
//        simpleRabbitListenerContainerFactory.setStartConsumerMinInterval();
//        simpleRabbitListenerContainerFactory.setStopConsumerMinInterval();
//        simpleRabbitListenerContainerFactory.setConsecutiveActiveTrigger();
//        simpleRabbitListenerContainerFactory.setConsecutiveIdleTrigger();
//        simpleRabbitListenerContainerFactory.setReceiveTimeout();
//        simpleRabbitListenerContainerFactory.setDeBatchingEnabled();
//        simpleRabbitListenerContainerFactory.setConnectionFactory();
//        simpleRabbitListenerContainerFactory.setErrorHandler();
//        simpleRabbitListenerContainerFactory.setMessageConverter();
//        simpleRabbitListenerContainerFactory.setAcknowledgeMode();
//        simpleRabbitListenerContainerFactory.setChannelTransacted();
//        simpleRabbitListenerContainerFactory.setTaskExecutor();
//        simpleRabbitListenerContainerFactory.setTransactionManager();
//        simpleRabbitListenerContainerFactory.setPrefetchCount();
//        simpleRabbitListenerContainerFactory.setDefaultRequeueRejected();
//        simpleRabbitListenerContainerFactory.setAdviceChain();
//        simpleRabbitListenerContainerFactory.setRecoveryInterval();
//        simpleRabbitListenerContainerFactory.setRecoveryBackOff();
//        simpleRabbitListenerContainerFactory.setMissingQueuesFatal();
//        simpleRabbitListenerContainerFactory.setMismatchedQueuesFatal();
//        simpleRabbitListenerContainerFactory.setConsumerTagStrategy();
//        simpleRabbitListenerContainerFactory.setIdleEventInterval();
//        simpleRabbitListenerContainerFactory.setFailedDeclarationRetryInterval();
//        simpleRabbitListenerContainerFactory.setApplicationEventPublisher();
//        simpleRabbitListenerContainerFactory.setApplicationContext();
//        simpleRabbitListenerContainerFactory.setAutoStartup();
//        simpleRabbitListenerContainerFactory.setPhase();
//        simpleRabbitListenerContainerFactory.setAfterReceivePostProcessors();
//        simpleRabbitListenerContainerFactory.setBeforeSendReplyPostProcessors();
        return simpleRabbitListenerContainerFactory;
    }
}
