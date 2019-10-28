package com.xing;

import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ServiceCombConsumerApplication
 *
 * @author guoxing
 * @date 10/28/2019 3:59 PM
 * @since 2.0.0
 **/
@SpringBootApplication
@EnableServiceComb
public class ServiceCombConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCombConsumerApplication.class, args);
    }
}
