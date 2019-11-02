package com.xing.testservicecombdemo;

import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableServiceComb //代表启动serviceComb应用程序
public class TestServiceCombDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestServiceCombDemoApplication.class, args);
	}
}
