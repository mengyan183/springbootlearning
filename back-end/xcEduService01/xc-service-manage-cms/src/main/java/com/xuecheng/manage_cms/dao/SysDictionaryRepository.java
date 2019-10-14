package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * SysDictionaryRepository
 *
 * @author guoxing
 * @date 10/14/2019 2:03 PM
 * @since 2.0.0
 **/
public interface SysDictionaryRepository extends MongoRepository<SysDictionary, String> {

    /**
     * 根据类型获取字典
     *
     * @param type
     * @return
     */
    SysDictionary findByDType(String type);
}
