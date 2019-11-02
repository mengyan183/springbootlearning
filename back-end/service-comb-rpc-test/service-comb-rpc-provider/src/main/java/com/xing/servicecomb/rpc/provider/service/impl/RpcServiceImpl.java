package com.xing.servicecomb.rpc.provider.service.impl;

import com.xing.servicecomb.rpc.service.RpcService;
import org.apache.servicecomb.provider.pojo.RpcSchema;

@RpcSchema(schemaId = "RpcServiceImpl")
public class RpcServiceImpl implements RpcService {
    /**
     * 第一个rpc interface method
     *
     * @param name
     * @return
     */
    @Override
    public String sayRpc(String name) {
        return "hello rpc " + name;
    }
}
