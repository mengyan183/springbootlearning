/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.manage_cms;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * GridFsTest
 *
 * @author guoxing
 * @date 8/29/2019 3:55 PM
 * @since 2.0.0
 **/
@SpringBootTest(classes = {ManageCmsApplication.class})
@RunWith(SpringRunner.class)
@Slf4j
public class GridFsTest {

    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;

    /**
     * 下载文件
     *
     * @throws IOException
     * @throws TemplateException
     */
    @Test
    public void testGetFile() throws IOException {
        String fileId = "5d68910cdf43e10d6ca9722c";
        //根据id查询文件
        GridFSFile gridFSFile =
                gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        if(gridFSFile == null){
            return;
        }
        //打开下载流对象
        GridFSDownloadStream gridFSDownloadStream =
                gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建gridFsResource，用于获取流对象
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        //获取流中的数据
        String s = IOUtils.toString(gridFsResource.getInputStream(), StandardCharsets.UTF_8);
        log.info(s);
    }

    @Test
    public void deleteFile(){
        String fileId = "5d68910cdf43e10d6ca9722c";
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is(fileId)));
    }

}
