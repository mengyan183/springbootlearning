package com.xuecheng.auth.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.auth.config.SystemConfig;
import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * AuthService
 *
 * @author guoxing
 * @date 10/24/2019 6:37 PM
 * @since 2.0.0
 **/
@Service
@Slf4j
public class AuthService {
    @Autowired
    private SystemConfig systemConfig;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 授权下发认证令牌
     *
     * @param loginRequest
     * @return
     */
    public AuthToken login(LoginRequest loginRequest) {
        if (loginRequest == null) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }

        // 从eureka中获取认证服务地址
        ServiceInstance choose = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        // 地址为 ip:port
        URI uri = choose.getUri();
        //1、header信息，包括了http basic认证信息
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        //将客户端id和客户端密码拼接，按“客户端id:客户端密码”
        String string = systemConfig.getAuthClientId() + ":" + systemConfig.getAuthClientSecret();
        //进行base64编码
        byte[] encode = Base64.encode(string.getBytes());
        // 加密 令牌申请 密码
        String httpbasic = "Basic " + new String(encode);
        //"Basic WGNXZWJBcHA6WGNXZWJBcHA="  设置认证服务器客户端认证码
        headers.add("Authorization", httpbasic);
        //2、包括:grant_type、username、password ;密码登录认证,接收请求中的用户名/密码
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username", loginRequest.getUsername());
        body.add("password", loginRequest.getPassword());
        // 组装 http请求参数组装包括 header和body
        HttpEntity<MultiValueMap<String, String>> multiValueMapHttpEntity = new HttpEntity<>(body, headers);
        // token 申请请求地址
        String authUrl = uri + "/auth/oauth/token";
        // 自定义响应结果处理
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                // 对 400 和 401 等 错误码正常返回
                if ("400".equalsIgnoreCase(response.getStatusCode().toString()) || "401".equalsIgnoreCase(response.getStatusCode().toString())) {
                    return;
                }
                super.handleError(response);
            }
        });
        // 发送 http请求 并使用map接收 ; 请求 授权地址
        ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, multiValueMapHttpEntity, Map.class);
        Map userTokenMap = exchange.getBody();
        if (userTokenMap == null || CollectionUtils.isEmpty(userTokenMap) || StringUtils.isBlank((String) userTokenMap.get("access_token")) || StringUtils.isBlank((String) userTokenMap.get("refresh_token")) || StringUtils.isBlank((String) userTokenMap.get("jti"))) {
            log.error("申请令牌获取到的数据为空;authUrl:{};请求参数为:{};返回的数据为:{}", authUrl, JSON.toJSONString(multiValueMapHttpEntity), JSON.toJSONString(userTokenMap));
            ExceptionCast.cast(CommonCode.FAIL);
        }
        AuthToken authToken = new AuthToken();
        authToken.setAccess_token((String) userTokenMap.get("jti"));
        authToken.setJwt_token((String) userTokenMap.get("access_token"));
        authToken.setRefresh_token((String) userTokenMap.get("refresh_token"));
        // 存入redis
        stringRedisTemplate.opsForValue().set("user_token" + authToken.getAccess_token(), JSON.toJSONString(authToken), Long.parseLong(systemConfig.getAuthTokenValiditySeconds()), TimeUnit.SECONDS);
        return authToken;
    }
}
