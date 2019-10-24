package com.xuecheng.auth;

import com.sun.jersey.core.util.Base64;
import com.xuecheng.framework.client.XcServiceList;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class AuthRestTest {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Test
    public void restAuth() {
        // 从eureka中获取认证服务地址
        ServiceInstance choose = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        // 地址为 ip:port
        URI uri = choose.getUri();
        //1、header信息，包括了http basic认证信息
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        String httpbasic = httpbasic("XcWebApp", "XcWebApp");
        //"Basic WGNXZWJBcHA6WGNXZWJBcHA="
        headers.add("Authorization", httpbasic);
        //2、包括:grant_type、username、passowrd
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username", "itcast");
        body.add("password", "123");
        HttpEntity<MultiValueMap<String, String>> multiValueMapHttpEntity = new HttpEntity<>(body, headers);

        String authUrl = uri + "/auth/oauth/token";
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if ("400".equalsIgnoreCase(response.getStatusText()) || "401".equalsIgnoreCase(response.getStatusText())) {
                    return;
                }
                super.handleError(response);
            }
        });
        ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, multiValueMapHttpEntity, Map.class);

        Map body1 = exchange.getBody();
        if (body1 != null) {
            log.info(body1.toString());
        }
    }

    private String httpbasic(String clientId, String clientSecret) {
        //将客户端id和客户端密码拼接，按“客户端id:客户端密码”
        String string = clientId + ":" + clientSecret;
        //进行base64编码
        byte[] encode = Base64.encode(string.getBytes());
        return "Basic " + new String(encode);
    }
}
