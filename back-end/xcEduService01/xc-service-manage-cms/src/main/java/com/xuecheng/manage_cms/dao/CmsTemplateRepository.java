/*
 * Copyright (c) 2019,crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * CmsTemplateRepository
 *
 * @author guoxing
 * @date 8/29/2019 10:05 AM
 * @since 2.0.0
 **/
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate,String> {
    /**
     * 根据站点id查询唯一模板
     *
     * @author guoxing
     * @date 2019-08-30 2:30 PM
     * @since 2.0.0
     **/
    List<CmsTemplate> findBySiteId(String siteId);
}
