package com.xuecheng.manage_course.client;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 1、feignClient接口 有参数在参数必须加@PathVariable("XXX")和@RequestParam("XXX")
 * 2、feignClient返回值为复杂对象时其类型必须有无参构造函数。
 */
@FeignClient(value = "xc-service-manage-cms")
@RequestMapping("/cms/page")
public interface CmsPageClient {
    /**
     * 远程调用cmspage 接口
     *
     * @param id
     * @return
     */
    //定义请求方式和地址
    @GetMapping("/get/{id}")
    CmsPage findById(@PathVariable("id") String id);

    /**
     * 远程调用cms page 添加方法
     *
     * @param cmsPage
     * @return
     */
    @PostMapping("/save")
    CmsPageResult save(@RequestBody CmsPage cmsPage);

    /**
     * 调用课程一键发布接口
     *
     * @param cmsPage
     * @return
     */
    @PostMapping("/postPageQuick")
    CmsPostPageResult postPageQuick(@RequestBody CmsPage cmsPage);
}
