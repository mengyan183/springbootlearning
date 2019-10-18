package com.xuecheng.manage_course.dao;

import com.xuecheng.manage_course.client.CmsPageClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class TetFeign {
    @Autowired
    private CmsPageClient cmsPageClient; // 接口代理对象

    @Test
    public void testCmsPageList() {
        cmsPageClient.findById("5a754adf6abb500ad05688d9");
    }
}
