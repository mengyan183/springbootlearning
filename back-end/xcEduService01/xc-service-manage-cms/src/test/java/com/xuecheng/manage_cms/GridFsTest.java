/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.manage_cms;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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

    //基于模板生成静态化文件
    @Test
    public void testGenerateHtml() throws IOException, TemplateException {
        //输出文件
        String classpath = this.getClass().getResource("/").getPath();
        File file = new File(classpath + "/templates/index_banner.ftl");
        ObjectId indexBannerId = gridFsTemplate.store(new FileInputStream(file), "index_banner");
        log.info(indexBannerId.toString());
    }

}
