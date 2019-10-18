/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * CmsConfigControllerApi
 *
 * @author guoxing
 * @date 8/29/2019 1:53 PM
 * @since 2.0.0
 **/
@Api(value="cms配置管理接口")
public interface CmsConfigControllerApi {

    /**
     * 根据pageId 获得 配置
     * @param id
     * @return
     */
    @ApiOperation("根据pageId 获得 配置")
    CmsConfig getById(String id);
}
