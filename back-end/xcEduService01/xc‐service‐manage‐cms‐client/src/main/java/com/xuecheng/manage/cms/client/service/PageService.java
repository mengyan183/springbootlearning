/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.manage.cms.client.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.manage.cms.client.dao.CmsPageRepository;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-09-12 18:32
 **/
@Service
public class PageService {
    private static Logger LOGGER = LoggerFactory.getLogger(PageService.class);
    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;
    @Autowired
    private SiteService siteService;

    /**
     * 保存html页面到服务器
     *
     * @author guoxing
     * @date 2019-09-03 9:56 AM
     * @since 2.0.0
     **/
    public void savePageToServerPath(String pageId) throws Exception {
        if (StringUtils.isBlank(pageId)) {
            LOGGER.error("pageId不能为空");
            return;
        }
        // 获取cms page
        CmsPage cmsPage = this.findById(pageId);
        if (cmsPage == null) {
            LOGGER.error("未找到指定cmsPage:{}", pageId);
            return;
        }
        // 得到文件id
        String htmlFileId = cmsPage.getHtmlFileId();
        if (StringUtils.isBlank(htmlFileId)) {
            LOGGER.error("cmsPage中文件id不存在:{}", JSON.toJSONString(cmsPage));
            return;
        }
        // 从gridfs中获取文件
        InputStream gridFsInputStream = getGridFsInputStream(htmlFileId);
        if (gridFsInputStream == null) {
            LOGGER.error("gridFs获取文件流不存在,fileId:{}", htmlFileId);
            return;
        }
        //获取站点
        String siteId = cmsPage.getSiteId();
        CmsSite cmsSite = siteService.findById(siteId);
        if (cmsSite == null) {
            LOGGER.error("cmsSite不存在,siteId:{}", siteId);
            return;
        }
        // 页面物理路径
        String pagePath = cmsSite.getSitePhysicalPath() + cmsPage.getPagePhysicalPath() + cmsPage.getPageName();
        if (StringUtils.isBlank(pagePath)) {
            LOGGER.error("站点页面物理路径不存在,cmsPage:{},cmsSite:{}", JSON.toJSONString(cmsPage), JSON.toJSONString(cmsSite));
            return;
        }
        //将html保存到文件服务器
        FileOutputStream fileOutputStream = new FileOutputStream(new File(pagePath));
        IOUtils.copy(gridFsInputStream, fileOutputStream);
        fileOutputStream.close();
    }

    /**
     * 根据pageId 获取 gridfs文件流
     *
     * @author guoxing
     * @date 2019-09-03 10:09 AM
     * @since 2.0.0
     **/
    private InputStream getGridFsInputStream(String fileId) throws Exception {
        // 从gridfs中获取文件
        //根据id查询文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        if (gridFSFile == null) {
            LOGGER.error("gridFSFile为空,fileId:{}", fileId);
            return null;
        }
        //打开下载流对象
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建gridFsResource，用于获取流对象
        return gridFSDownloadStream != null ? new GridFsResource(gridFSFile, gridFSDownloadStream).getInputStream() : null;
    }

    /**
     * 根据页面id获取cmsPage
     *
     * @author guoxing
     * @date 2019-09-03 10:05 AM
     * @since 2.0.0
     **/
    public CmsPage findById(String pageId) {
        if (StringUtils.isBlank(pageId)) {
            return null;
        }
        // 获取cms page
        Optional<CmsPage> cmsPageOptional = cmsPageRepository.findById(pageId);
        return cmsPageOptional.orElse(null);
    }


}
