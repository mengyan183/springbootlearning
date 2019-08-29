/*
 * Copyright (c) 2019, Jiehun.com.cn Inc. All Rights Reserved
 */
package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsConfigControllerApi;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import com.xuecheng.manage_cms.service.CmsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CmsConfigController
 *
 * @author guoxing
 * @date 8/29/2019 1:57 PM
 * @since 2.0.0
 **/
@RestController
@RequestMapping("/cms/config")
public class CmsConfigController implements CmsConfigControllerApi {
    @Autowired
    private CmsConfigService cmsConfigService;
    /**
     * 根据pageId 获得 配置
     *
     * @param id
     * @return
     */
    @Override
    @GetMapping("/getmodel/{pageId}")
    public CmsConfig getById(@PathVariable("pageId") String id) {
        return cmsConfigService.getById(id);
    }
}
