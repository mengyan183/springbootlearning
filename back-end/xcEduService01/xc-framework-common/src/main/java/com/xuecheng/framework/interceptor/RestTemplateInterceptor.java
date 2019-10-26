package com.xuecheng.framework.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 自定义interceptor 拦截器
 */
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        HttpHeaders headers = request.getHeaders();
        while (headerNames.hasMoreElements()) {
            String s = headerNames.nextElement();
            if ("authorization".equalsIgnoreCase(s)) {
                // 获取到jwt令牌信息
                String authorization = httpServletRequest.getHeader(s);
                if(StringUtils.isNotBlank(authorization)){
                    // 并将令牌信息传递下去
                    headers.add(s,authorization);
                }
            }
        }
        return execution.execute(request,body);
    }
}
