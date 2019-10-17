package com.xuecheng.manage_course.client;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *1、feignClient接口 有参数在参数必须加@PathVariable("XXX")和@RequestParam("XXX")
 * 2、feignClient返回值为复杂对象时其类型必须有无参构造函数。
 */
@FeignClient(value = "xc-service-manage-cms")
public interface CmsPageClient {
    /**
     * 远程调用cmspage 接口
     *
     * @param id
     * @return
     */
    @GetMapping("/cms/page/get/{id}")//定义请求方式和地址
    CmsPage findById(@PathVariable("id") String id);
}
