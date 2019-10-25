package com.xuecheng.api.ucenter;

import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * UcenterControllerApi
 *
 * @author guoxing
 * @date 10/25/2019 11:16 AM
 * @since 2.0.0
 **/
@Api(value = "用户中心", tags = "用户中心管理")
public interface UcenterControllerApi {
    @ApiOperation("根据用户名获取用户信息")
    XcUserExt getUserext(String username);
}
