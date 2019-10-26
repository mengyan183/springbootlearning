package com.xuecheng.ucenter.service;

import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void getUserExt() {
        XcUserExt itcast = userService.getUserExt("itcast");
        log.info(itcast.toString());
    }
}
