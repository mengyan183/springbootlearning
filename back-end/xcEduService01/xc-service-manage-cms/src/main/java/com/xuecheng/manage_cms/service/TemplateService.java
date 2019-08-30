/*
 * Copyright (c) 2019,crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.manage_cms.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * TemplateService
 * cms 模板数据 服务
 * @author guoxing
 * @date 8/29/2019 10:04 AM
 * @since 2.0.0
 **/
@Service
public class TemplateService {
    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;

    public List<CmsTemplate> listAll(){
        return cmsTemplateRepository.findAll();
    }

    /**
     * 新增模板
     *
     * @author guoxing
     * @date 2019-08-30 1:57 PM
     * @since 2.0.0
     **/
    public CmsTemplate add(CmsTemplate cmsTemplate) {
        if (cmsTemplate == null) {
            return null;
        }
        if (StringUtils.isBlank(cmsTemplate.getSiteId())) {
            return null;
        }
        return cmsTemplateRepository.save(cmsTemplate);
    }

    /**
     * 根据站点id获取模板
     *
     * @author guoxing
     * @date 2019-08-30 1:57 PM
     * @since 2.0.0
     **/
    public List<CmsTemplate> listBySiteId(String siteId) {
        if (StringUtils.isBlank(siteId)) {
            return null;
        }
        // 可以获取得到唯一的数据
//        CmsTemplate cmsTemplate = new CmsTemplate();
//        cmsTemplate.setSiteId(siteId);
//        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("siteId", ExampleMatcher.GenericPropertyMatchers.storeDefaultMatching());
//        Example<CmsTemplate> example = Example.of(cmsTemplate, matcher);
//        Optional<CmsTemplate> one = cmsTemplateRepository.findOne(example);
//        return one.orElse(null);
        // 如果返回唯一的数据 如果存在 重复数据 会 上抛异常信息
        return cmsTemplateRepository.findBySiteId(siteId);
    }

    /**
     * 根据资源id获取
     *
     * @author guoxing
     * @date 2019-08-30 3:02 PM
     * @since 2.0.0
     **/
    public CmsTemplate getById(String templateId) {
        if (StringUtils.isBlank(templateId)) {
            return null;
        }
        return cmsTemplateRepository.findById(templateId).orElse(null);
    }

    public String getTemplateContentById(String templateId) {
        if (StringUtils.isBlank(templateId)) {
            return null;
        }
        // 获取 模板 数据
        CmsTemplate cmsTemplate = this.getById(templateId);
        if (cmsTemplate == null) {
            return null;
        }
        //模板文件id
        String templateFileId = cmsTemplate.getTemplateFileId();
        if (StringUtils.isBlank(templateFileId)) {
            return null;
        }
        //取出模板文件内容
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
        if (gridFSFile == null) {
            return null;
        }
        //打开下载流对象
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        if (gridFSDownloadStream == null) {
            return null;
        }
        //创建GridFsResource
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        try {
            return IOUtils.toString(gridFsResource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }
}
