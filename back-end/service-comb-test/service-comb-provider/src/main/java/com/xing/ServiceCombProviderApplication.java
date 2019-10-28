package com.xing;

import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ServiceCombProviderApplication
 *
 * @author guoxing
 * @date 10/28/2019 4:00 PM
 * @since 2.0.0
 **/
@SpringBootApplication
@EnableServiceComb
public class ServiceCombProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCombProviderApplication.class, args);
    }
}
