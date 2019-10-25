package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.CookieUtil;
import com.xuecheng.govern.gateway.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * LoginFilter
 * 用户身份校验过滤器
 *
 * @author guoxing
 * @date 10/25/2019 4:02 PM
 * @since 2.0.0
 **/
@Configuration
public class LoginFilter extends ZuulFilter {

    @Autowired
    private AuthService authService;

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
        return true;
    }

    /**
     * 过滤器拦截具体执行逻辑
     *
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        // 从请求中获取指定cookie和header信息;调用auth服务实现登录认证
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        HttpServletResponse response = currentContext.getResponse();
        // 从header中获取authorization(存储的是 用户 jwt 令牌信息)
        String authorization = request.getHeader("Authorization");
        // 从cookie中获取 自定义uid token信息
        //cookie中存储的uid 为 该用户的身份认证jti token信息
        Map<String, String> map = CookieUtil.readCookie(request, "uid");
        if (StringUtils.isBlank(authorization) || !(map.containsKey("uid") && StringUtils.isNotBlank(map.get("uid")))) {
            // 拒绝访问
            currentContext.setSendZuulResponse(false);
            // 设置响应状态码
            currentContext.setResponseStatusCode(200);
            ResponseResult unauthenticated = new ResponseResult(CommonCode.UNAUTHENTICATED);
            String jsonString = JSON.toJSONString(unauthenticated);
            currentContext.setResponseBody(jsonString);
            response.setContentType("application/json;charset=UTF‐8");
            return null;
        }
        try {
            boolean flag = authService.verifyLoginUser(authorization, map.get("uid"));
            if (!flag) {
                // 拒绝访问
                currentContext.setSendZuulResponse(false);
                // 设置响应状态码
                currentContext.setResponseStatusCode(200);
                ResponseResult unauthenticated = new ResponseResult(CommonCode.UNAUTHENTICATED);
                String jsonString = JSON.toJSONString(unauthenticated);
                currentContext.setResponseBody(jsonString);
                response.setContentType("application/json;charset=UTF‐8");
            }
        } catch (Exception e) {
            // 拒绝访问
            currentContext.setSendZuulResponse(false);
            // 设置响应状态码
            currentContext.setResponseStatusCode(200);
            ResponseResult unauthenticated = new ResponseResult();
            unauthenticated.setMessage(e.getMessage());
            unauthenticated.setSuccess(false);
            String jsonString = JSON.toJSONString(unauthenticated);
            currentContext.setResponseBody(jsonString);
            response.setContentType("application/json;charset=UTF‐8");
            return null;
        }

        return null;
    }
}
