package com.xuecheng.framework.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 定义feignclient 微服务远程调用拦截器
 */
public class FeignClientInterceptor implements RequestInterceptor {

    /**
     * 所有的微服务feign 调用都会经过该方法
     *
     * @param requestTemplate
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        //TODO : RequestContextHolder理解使用??
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String s = headerNames.nextElement();
            if ("authorization".equalsIgnoreCase(s)) {
                // 获取到jwt令牌信息
                String authorization = request.getHeader(s);
                if(StringUtils.isNotBlank(authorization)){
                    // 并将令牌信息传递下去
                    requestTemplate.header(s,authorization);
                }
            }
        }

    }
}
