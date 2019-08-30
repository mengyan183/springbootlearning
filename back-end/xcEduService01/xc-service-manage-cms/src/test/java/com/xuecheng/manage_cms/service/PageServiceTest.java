/*
 * Copyright (c) 2019,crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.manage_cms.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * TemplateService
 *
 * @author guoxing
 * @date 8/30/2019 11:42 AM
 * @since 2.0.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j

public class PageServiceTest {

    @Autowired
    private PageService pageService;

    @Test
    public void getPageHtmlByPageId() throws Exception {
        String pageId = "5a795ac7dd573c04508f3a56";
        String pageHtmlByPageId = pageService.getPageHtmlByPageId(pageId);
        log.info(pageHtmlByPageId);
    }
}
