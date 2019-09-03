/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.manage.cms.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * ManageCmsClientApplication
 *
 * @author guoxing
 * @date 9/3/2019 9:35 AM
 * @since 2.0.0
 **/
@SpringBootApplication
//实体扫描
@EntityScan(basePackages = {"com.xuecheng.framework.domain.cms"})
//扫描common下的所有类
@ComponentScan(basePackages = {"com.xuecheng.framework"})
//扫描当前工程
@ComponentScan(basePackages = {"com.xuecheng.manage.cms.client"})
public class ManageCmsClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageCmsClientApplication.class, args);
    }
}
