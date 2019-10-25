package com.xuecheng.govern.gateway.client;

import com.xuecheng.framework.client.XcServiceList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * AuthServiceClient
 *
 * @author guoxing
 * @date 10/25/2019 4:47 PM
 * @since 2.0.0
 **/
@FeignClient(XcServiceList.XC_SERVICE_UCENTER_AUTH)
public interface AuthServiceClient {

    /**
     * 远程调用获取用户详细token
     *
     * @param accessToken 简单token信息
     * @return 完整token信息
     */
    @GetMapping("/auth/gettokenbyaccesstoken")
    String getTokenByAccessToken(@RequestParam("accessToken") String accessToken);

}
