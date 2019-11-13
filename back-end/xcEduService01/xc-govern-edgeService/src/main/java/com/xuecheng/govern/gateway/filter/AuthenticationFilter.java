package com.xuecheng.govern.gateway.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.servicecomb.common.rest.filter.HttpServerFilter;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.foundation.vertx.http.HttpServletRequestEx;
import org.apache.servicecomb.swagger.invocation.Response;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.http.HttpHeaders;
import javax.ws.rs.core.Response.Status;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-08-22 18:24
 **/
public class AuthenticationFilter implements HttpServerFilter {

    //定义公开微服务列表（无需携带token即可访问），使用set存储公开微服务的名称
    private static final Set<String> NOT_REQUIRED_VERIFICATION_SERVICE_NAMES = new HashSet<String>(
            Arrays.asList("xc-service-search","xc-service-portalview","xc-service-learning")
            );

    //返回过虑器执行顺序号，越小则越优先执行
    @Override
    public int getOrder() {
        return 0;
    }

    //功能描述：针对需要校验用户身份的请求url会校验是否携带token，如果无需校验身份的url（公开地址）则放行
    @Override
    public Response afterReceiveRequest(Invocation invocation, HttpServletRequestEx httpServletRequestEx) {
        //如果需要校验用户身份则判断是否携带token
        //请求微服务的名称
        String microserviceName = invocation.getMicroserviceName();
        //判断微服务名称是否在公开服务列表中存在，如果不存在则说明需要校验token
        if(isInvocationNeedValidate(microserviceName)){
            //取出token
            String token = httpServletRequestEx.getHeader(HttpHeaders.AUTHORIZATION);
            if(StringUtils.isNotEmpty(token)){
                //校验token
                //....
            }else{
                //返回校验失败信息
                return Response.failResp(new InvocationException(Status.UNAUTHORIZED,"authentication failed"));
            }
        }
        return null;
    }

    //是否需要校验token
    private boolean isInvocationNeedValidate(String serviceName){
        for(String service_name:NOT_REQUIRED_VERIFICATION_SERVICE_NAMES){
            if(serviceName.equals(service_name)){
                return false;
            }
        }
        return true;
    }
}
