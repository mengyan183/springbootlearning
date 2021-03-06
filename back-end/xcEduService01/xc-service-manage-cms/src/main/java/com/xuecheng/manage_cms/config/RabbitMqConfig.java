/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.manage_cms.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * RabbitMqConfig
 *
 * @author guoxing
 * @date 9/3/2019 9:48 AM
 * @since 2.0.0
 **/
@Component
public class RabbitMqConfig {
    //交换机的名称
    public static final String EX_ROUTING_CMS_POSTPAGE = "ex_routing_cms_postpage";

    /**
     * 交换机配置使用direct类型
     *
     * @return the exchange
     */
    @Bean(EX_ROUTING_CMS_POSTPAGE)
    public Exchange EXCHANGE_TOPICS_INFORM() {
        return ExchangeBuilder.directExchange(EX_ROUTING_CMS_POSTPAGE).durable(true).build();
    }

}
