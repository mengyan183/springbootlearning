/*
 * Copyright (c) 2019,crayonshinchanxingguo.com Inc. All Rights Reserved
 */

import com.xuecheng.test.freemarker.FreemarkerTestApplication;
import com.xuecheng.test.freemarker.model.Student;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.*;
import java.util.*;

/**
 * GridFsTest
 *
 * @author guoxing
 * @date 8/29/2019 3:55 PM
 * @since 2.0.0
 **/
@SpringBootTest(classes = {FreemarkerTestApplication.class})
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
