/*
 * Copyright (c) 2019, Jiehun.com.cn Inc. All Rights Reserved
 */
package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SiteService
 *
 * @author guoxing
 * @date 7/31/2019 2:42 PM
 * @since 2.0.0
 **/
@Service
public class SiteService {
    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    public List<CmsSite> listAll() {
        return cmsSiteRepository.findAll();
    }
}
