package com.xuecheng.api.auth;

import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "用户认证接口", tags = "用户认证接口")
public interface AuthControllerApi {
    @ApiOperation("登录")
    LoginResult login(LoginRequest loginRequest, HttpServletResponse response);

    @ApiOperation("退出")
    ResponseResult logout(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse);

    @ApiOperation("查询用户jwt令牌")
    JwtResult getUserJwt(HttpServletRequest httpServletRequest);

    @ApiOperation("根据用户accesstoken获取redis存储的完整token信息")
    String getTokenByAccessToken(String accessToken);

}
