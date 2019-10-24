package com.xuecheng.auth.controller;

import com.xuecheng.api.auth.AuthControllerApi;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.model.response.ResponseResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController implements AuthControllerApi {

    /**
     * 登录
     *
     * @param loginRequest
     * @return
     */
    @Override
    public LoginResult login(LoginRequest loginRequest) {
        return null;
    }

    /**
     * 退出登录
     *
     * @return
     */
    @Override
    public ResponseResult logout() {
        return null;
    }
}
