package com.xing.servicecomb.rpc.consumer.controller;

import com.xing.servicecomb.rpc.service.RpcService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class RpcController {

    @Autowired
    private RpcService rpcService;

    @GetMapping("/sayrpc")
    public String sayRpc(String name) {
        if (StringUtils.isBlank(name)) {
            name = "test";
        }
        return rpcService.sayRpc(name);
    }

}
