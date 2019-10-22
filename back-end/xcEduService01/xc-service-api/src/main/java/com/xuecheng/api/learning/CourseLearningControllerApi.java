package com.xuecheng.api.learning;

import com.xuecheng.framework.domain.learning.response.GetMediaResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * CourseLearningControllerApi
 *
 * @author guoxing
 * @date 10/22/2019 9:56 AM
 * @since 2.0.0
 **/
@Api(value = "录播课程学习管理", tags = "录播课程学习管理")
public interface CourseLearningControllerApi {
    @ApiOperation("获取课程学习地址")
    GetMediaResult getMedia(String courseId, String teachplanId);
}
