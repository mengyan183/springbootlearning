package com.xuecheng.manage.cms.client.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * CmsSiteRepository
 *
 * @author guoxing
 * @date 7/31/2019 2:43 PM
 * @since 2.0.0
 **/
public interface CmsSiteRepository extends MongoRepository<CmsSite, String> {
}
