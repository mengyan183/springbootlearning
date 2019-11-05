/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.manage.cms.client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.manage.cms.client.service.PageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * ConsumerPostPage
 *
 * @author guoxing
 * @date 9/3/2019 10:51 AM
 * @since 2.0.0
 **/
@Component
public class ConsumerPostPage {
    private static Logger LOGGER = LoggerFactory.getLogger(ConsumerPostPage.class);
    @Autowired
    private PageService pageService;

    /**
     * 发布页面
     *
     * @author guoxing
     * @date 2019-09-03 10:54 AM
     * @since 2.0.0
     **/
    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPage(String msg) {
        //解析 msg ; 消息格式 为
        /**
         * {
         * "pageId":""
         * }
         */
        Map map = JSON.parseObject(msg, Map.class);
        if (map == null) {
            LOGGER.error("消息为空");
            return;
        }
        // 获取消息中的 站点id
        String pageId = (String) map.get("pageId");
        if (StringUtils.isBlank(pageId)) {
            LOGGER.error("mq消息中pageId 为 空");
            return;
        }
        // 将文件从gridFS中下载到服务器
        try {
            pageService.savePageToServerPath(pageId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
