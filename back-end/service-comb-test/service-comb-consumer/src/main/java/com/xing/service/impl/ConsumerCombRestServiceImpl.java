package com.xing.service.impl;

import com.xing.service.CombRestService;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * ConsumerCombRestServiceImpl
 * combservice 消费者
 *
 * @author guoxing
 * @date 10/28/2019 4:30 PM
 * @since 2.0.0
 **/
@Service
public class ConsumerCombRestServiceImpl implements CombRestService {
    private final RestTemplate restTemplate = RestTemplateBuilder.create();

    /**
     * 首次调用rest
     *
     * @param name
     * @return
     */
    @Override
    public String sayRest(String name) {
        //service url is : cse://serviceName/operation  serviceName 指代的是 service_description:name 设置的名称
        String serviceName = "ServiceCombTestProvider";
        // 微服务之间调用方式
        return restTemplate.getForObject("cse://" + serviceName + "/say?name=" + name, String.class);
    }
}
