package com.xing.testservicecombdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;

@SpringBootApplication
@EnableServiceComb
public class TestServiceCombDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestServiceCombDemoApplication.class, args);
	}
}
