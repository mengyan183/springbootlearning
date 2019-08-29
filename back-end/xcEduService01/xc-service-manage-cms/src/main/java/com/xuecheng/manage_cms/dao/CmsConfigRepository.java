package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * CmsConfigRepository
 *
 * @author guoxing
 * @date 8/29/2019 1:56 PM
 * @since 2.0.0
 **/
public interface CmsConfigRepository extends MongoRepository<CmsConfig,String> {
}
