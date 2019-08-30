/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * CmsConfigService
 *
 * @author guoxing
 * @date 8/29/2019 1:54 PM
 * @since 2.0.0
 **/
@Service
public class CmsConfigService {
    @Autowired
    private CmsConfigRepository cmsConfigRepository;

    public CmsConfig getById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        Optional<CmsConfig> cmsConfig = cmsConfigRepository.findById(id);
        return cmsConfig.orElse(null);
    }

}
