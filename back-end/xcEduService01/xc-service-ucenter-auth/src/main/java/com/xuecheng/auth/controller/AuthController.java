package com.xuecheng.auth.controller;

import com.xuecheng.api.auth.AuthControllerApi;
import com.xuecheng.auth.config.SystemConfig;
import com.xuecheng.auth.service.AuthService;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author guoxing
 * @date 2019-10-24 6:36 PM
 * @since 2.0.0
 **/
@RestController
@RequestMapping("/")
@Slf4j
public class AuthController implements AuthControllerApi {
    @Autowired
    private AuthService authService;
    @Autowired
    private SystemConfig systemConfig;

    /**
     * 登录
     *
     * @param loginRequest
     * @return
     */
    @Override
    @PostMapping("/userlogin")
    public LoginResult login(LoginRequest loginRequest, HttpServletResponse response) {
        // 申请令牌
        AuthToken authToken = authService.login(loginRequest);
        // 将令牌写入cookie
        if (authToken == null || StringUtils.isEmpty(authToken.getJwt_token())) {
            return new LoginResult(AuthCode.AUTH_LOGIN_ERROR, null);
        }
        //添加cookie 认证令牌，最后一个参数设置为false，表示允许浏览器获取
        CookieUtil.addCookie(response, systemConfig.getCookieDomain(), "/", "uid", authToken.getAccess_token(), Integer.parseInt(systemConfig.getCookieMaxAge()), false);
        return new LoginResult(CommonCode.SUCCESS, authToken.getAccess_token());
    }



    /**
     * 退出登录
     *
     * @return
     */
    @Override
    @GetMapping("/userlogout")
    public ResponseResult logout(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
        Map<String, String> cookieMap = CookieUtil.readCookie(httpServletRequest, "uid");
        authService.logout(cookieMap.get("uid"));
        //清除cookie
        CookieUtil.addCookie(httpServletResponse, systemConfig.getCookieDomain(), "/", "uid", null, 0, false);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    @GetMapping("/getuserjwt")
    public JwtResult getUserJwt(HttpServletRequest httpServletRequest) {
        // 用户token
        Map<String, String> cookieMap = CookieUtil.readCookie(httpServletRequest, "uid");
        return authService.getUserJwt(cookieMap.get("uid"));
    }

    @Override
    @GetMapping("/gettokenbyaccesstoken")
    public String getTokenByAccessToken(@RequestParam("accessToken") String accessToken){
        return authService.getTokenByAccessToken(accessToken);
    }
}
