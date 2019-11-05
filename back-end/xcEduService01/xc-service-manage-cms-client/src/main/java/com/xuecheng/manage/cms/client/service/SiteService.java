/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.manage.cms.client.service;

import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.manage.cms.client.dao.CmsSiteRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-09-12 18:32
 **/
@Service
public class SiteService {
    private static Logger LOGGER = LoggerFactory.getLogger(SiteService.class);
    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    /**
     * 根据站点id获取站点
     *
     * @author guoxing
     * @date 2019-09-03 10:05 AM
     * @since 2.0.0
     **/
    public CmsSite findById(String siteId) {
        if (StringUtils.isBlank(siteId)) {
            return null;
        }
        // 获取cms page
        Optional<CmsSite> cmsSiteOptional = cmsSiteRepository.findById(siteId);
        return cmsSiteOptional.orElse(null);
    }


}
