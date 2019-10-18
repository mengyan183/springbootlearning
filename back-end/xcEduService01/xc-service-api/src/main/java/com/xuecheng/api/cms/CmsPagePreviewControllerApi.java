package com.xuecheng.api.cms;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

/**
 * CmsPagePreviewControllerApi
 *
 * @author guoxing
 * @date 10/18/2019 11:40 AM
 * @since 2.0.0
 **/
@Api(value = "cms页面静态化预览接口")
public interface CmsPagePreviewControllerApi {

    @ApiOperation("静态化页面预览")
    void previewPageByPageId(String pageId, HttpServletResponse httpServletResponse);
}
