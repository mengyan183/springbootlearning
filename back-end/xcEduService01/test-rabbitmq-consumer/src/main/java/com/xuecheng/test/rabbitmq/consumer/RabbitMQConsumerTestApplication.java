/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.test.rabbitmq.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * RabbitMQConsumerTestApplication
 *
 * @author guoxing
 * @date 9/2/2019 9:51 AM
 * @since 2.0.0
 **/
@SpringBootApplication
public class RabbitMQConsumerTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(RabbitMQConsumerTestApplication.class,args);
    }
}
