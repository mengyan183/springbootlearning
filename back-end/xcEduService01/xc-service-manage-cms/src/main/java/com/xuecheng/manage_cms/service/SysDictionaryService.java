package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.dao.SysDictionaryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SysDictionaryService
 * 数据字典 服务
 * @author guoxing
 * @date 10/14/2019 1:59 PM
 * @since 2.0.0
 **/
@Service
public class SysDictionaryService {
    @Autowired
    private SysDictionaryRepository sysDictionaryRepository;

    public SysDictionary getByType(String type) {
        if(StringUtils.isBlank(type)){
            return null;
        }
        return sysDictionaryRepository.findByDType(type);
    }
}
