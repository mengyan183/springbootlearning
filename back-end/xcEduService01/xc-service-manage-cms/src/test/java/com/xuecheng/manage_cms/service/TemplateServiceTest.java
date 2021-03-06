/*
 * Copyright (c) 2019,crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsTemplate;
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
public class TemplateServiceTest {

    @Autowired
    private TemplateService templateService;

    @Test
    public void addTemplate() throws Exception {
        CmsTemplate cmsTemplate = new CmsTemplate();
        cmsTemplate.setSiteId("5a751fab6abb5044e0d19ea1");
        cmsTemplate.setTemplateName("测试模板");
        cmsTemplate.setTemplateParameter("");
        cmsTemplate.setTemplateFileId("5d68d747df43e1b23ce3bc25");
        templateService.add(cmsTemplate);
    }




}
