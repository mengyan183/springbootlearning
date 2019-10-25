package com.xuecheng.auth.client;

import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * UserServiceClient
 * 调用ucenter 服务
 *
 * @author guoxing
 * @date 10/25/2019 11:34 AM
 * @since 2.0.0
 **/
@FeignClient(XcServiceList.XC_SERVICE_UCENTER)
public interface UserServiceClient {

    /**
     * 获取 ucenter中的用户信息
     *
     * @param username
     * @return
     */
    @GetMapping("/ucenter/getuserext")
    XcUserExt getUserext(@RequestParam("username") String username);
}
