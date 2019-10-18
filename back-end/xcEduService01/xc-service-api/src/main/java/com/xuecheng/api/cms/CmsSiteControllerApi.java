package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsSite;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @author xing
 */
@Api(value = "cms站点管理接口")
public interface CmsSiteControllerApi {
    @ApiOperation("列表")
    List<CmsSite> findList();
}
