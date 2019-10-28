package com.xing.controller;

import com.xing.service.CombRestService;
import org.apache.commons.lang3.StringUtils;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ConsumerCombRestController
 *
 * @author guoxing
 * @date 10/28/2019 4:37 PM
 * @since 2.0.0
 **/
@RestSchema(schemaId = "ConsumerCombRestController")
@RequestMapping("/")
public class ConsumerCombRestController {
    @Autowired
    private CombRestService combRestService;

    @GetMapping("/say")
    public String sayRest(String name) {
        if (StringUtils.isBlank(name)) {
            name = "";
        }
        return combRestService.sayRest(name);
    }

}
