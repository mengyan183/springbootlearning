package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * CategoryControllerApi
 *
 * @author guoxing
 * @date 10/14/2019 11:34 AM
 * @since 2.0.0
 **/
@Api("课程分类")
public interface CategoryControllerApi {
    @ApiOperation("查询分类")
    public CategoryNode findList();
}
