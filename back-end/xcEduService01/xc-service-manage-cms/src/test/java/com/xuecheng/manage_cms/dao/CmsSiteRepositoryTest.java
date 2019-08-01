package com.xuecheng.manage_cms.dao;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsSite;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-09-12 18:11
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class CmsSiteRepositoryTest {

    @Autowired
    CmsSiteRepository cmsSiteRepository;

    @Test
    public void testFindAll() {
        List<CmsSite> all = cmsSiteRepository.findAll();
        log.info(JSON.toJSONString(all));
    }
}
