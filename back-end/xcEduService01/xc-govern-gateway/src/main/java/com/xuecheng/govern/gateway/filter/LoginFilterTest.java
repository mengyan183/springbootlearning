package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * LoginFilterTest
 *
 * @author guoxing
 * @date 10/25/2019 4:02 PM
 * @since 2.0.0
 **/
@Configuration
public class LoginFilterTest extends ZuulFilter {
    @Override
    public String filterType() {
        /**
         * 类型总共包含四种
         * 1: pre:在路由请求之前执行
         * 2: routing:在路由请求时执行
         * 3: post:在路由请求之后执行
         * 4: error:在路由请求报错后执行
         */

        return "pre";
    }

    /**
     * 过滤器序号越小,执行越靠前
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 是否执行该过滤器
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return false;
    }

    /**
     * 过滤器拦截具体执行逻辑
     *
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        /**
         * TODO :测试代码
         * 模拟测试如果当前请求header中不包含Authorization,则自定义响应内容
         */
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletResponse response = requestContext.getResponse();
        HttpServletRequest request = requestContext.getRequest();
        //取出头部信息Authorization
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isBlank(authorization)) {
            // 拒绝访问
            requestContext.setSendZuulResponse(false);
            // 设置响应状态码
            requestContext.setResponseStatusCode(200);
            ResponseResult unauthenticated = new ResponseResult(CommonCode.UNAUTHENTICATED);
            String jsonString = JSON.toJSONString(unauthenticated);
            requestContext.setResponseBody(jsonString);
            response.setContentType("application/json;charset=UTF‐8");
            return null;
        }
        return null;
    }
}
