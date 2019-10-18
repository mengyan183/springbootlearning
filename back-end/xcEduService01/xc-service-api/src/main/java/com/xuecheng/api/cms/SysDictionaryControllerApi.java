package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * SysDicthinaryControllerApi
 *
 * @author guoxing
 * @date 10/14/2019 1:57 PM
 * @since 2.0.0
 **/
@Api(value = "数据字典接口")
public interface SysDictionaryControllerApi {
    /**
     * 数据字典查询
     *
     * @param type
     * @return
     */
    @ApiOperation(value = "数据字典查询接口")
    public SysDictionary getByType(String type);
}
