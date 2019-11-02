package com.xing.servicecomb.rpc.consumer;

import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableServiceComb
public class ServiceCombRpcConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCombRpcConsumerApplication.class, args);
    }
}
