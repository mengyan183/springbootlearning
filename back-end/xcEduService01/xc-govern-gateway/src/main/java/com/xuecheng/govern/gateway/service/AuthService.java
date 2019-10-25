package com.xuecheng.govern.gateway.service;

import com.xuecheng.govern.gateway.client.AuthServiceClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AuthService
 *
 * @author guoxing
 * @date 10/25/2019 4:30 PM
 * @since 2.0.0
 **/
@Service
public class AuthService {
    @Autowired
    private AuthServiceClient authServiceClient;


    /**
     * 判断当前请求中携带的认证信息是否符合条件
     *
     * @param authorization 用户详细token信息
     * @param jwtToken      用户短token信息
     * @return 校验结果
     */
    public boolean verifyLoginUser(String authorization, String jwtToken) {
        if (StringUtils.isBlank(authorization) || StringUtils.isBlank(jwtToken)) {
            return false;
        }
        String tokenByAccessToken = authServiceClient.getTokenByAccessToken(jwtToken);
        if (StringUtils.isBlank(tokenByAccessToken)) {
            return false;
        }
        tokenByAccessToken += "Bearer ";
        return tokenByAccessToken.equalsIgnoreCase(authorization);
    }
}
