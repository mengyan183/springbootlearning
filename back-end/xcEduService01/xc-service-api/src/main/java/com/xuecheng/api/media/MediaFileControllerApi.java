package com.xuecheng.api.media;

import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * MediaFileControllerApi
 *
 * @author guoxing
 * @date 10/21/2019 10:21 AM
 * @since 2.0.0
 **/
@Api(value = "媒体文件管理", tags = {"媒体文件管理接口"})
public interface MediaFileControllerApi {

    /**
     * 媒体文件 列表 分页查询
     *
     * @param page
     * @param size
     * @param queryMediaFileRequest
     * @return
     */
    @ApiOperation("查询文件列表")
    QueryResponseResult findList(Integer page, Integer size, QueryMediaFileRequest queryMediaFileRequest);
}
