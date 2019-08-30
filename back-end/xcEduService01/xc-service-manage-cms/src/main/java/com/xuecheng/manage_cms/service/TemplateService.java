/*
 * Copyright (c) 2019,crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TemplateService
 * cms 模板数据 服务
 * @author guoxing
 * @date 8/29/2019 10:04 AM
 * @since 2.0.0
 **/
@Service
public class TemplateService {
    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

    public List<CmsTemplate> listAll(){
        return cmsTemplateRepository.findAll();
    }
}
