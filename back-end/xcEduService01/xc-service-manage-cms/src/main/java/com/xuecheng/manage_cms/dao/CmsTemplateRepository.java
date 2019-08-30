/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * CmsTemplateRepository
 *
 * @author guoxing
 * @date 8/29/2019 10:05 AM
 * @since 2.0.0
 **/
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate,String> {
}
