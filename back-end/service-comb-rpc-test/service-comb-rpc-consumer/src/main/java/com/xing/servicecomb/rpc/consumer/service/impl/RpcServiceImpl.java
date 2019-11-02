package com.xing.servicecomb.rpc.consumer.service.impl;

import com.xing.servicecomb.rpc.service.RpcService;
import org.apache.servicecomb.provider.pojo.RpcReference;
import org.springframework.stereotype.Component;

@Component
public class RpcServiceImpl implements RpcService {

    // 从注册中心查找指定应用名称下的rpc服务
    /**
     * microserviceName : 指定应用程序名称和服务名称, 指定服务microservice.yaml 下的APPLICATION_ID:service_description.name
     * schemaId: 代表服务编号
     */
    @RpcReference(schemaId = "RpcServiceImpl", microserviceName = "ServiceCombRpc:ServiceCombRpcProvider")
    private RpcService rpcService;

    /**
     * 第一个rpc interface method
     *
     * @param name
     * @return
     */
    @Override
    public String sayRpc(String name) {
        return rpcService.sayRpc(name);
    }
}
