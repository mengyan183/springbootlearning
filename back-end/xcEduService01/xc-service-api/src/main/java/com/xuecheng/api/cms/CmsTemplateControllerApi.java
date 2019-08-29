/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsTemplate;

import java.util.List;

/**
 * CmsTemplateControllerApi
 *
 * @author guoxing
 * @date 8/29/2019 10:02 AM
 * @since 2.0.0
 **/
public interface CmsTemplateControllerApi {
    /**
     * 获取所有的cms模板数据
     *
     * @author guoxing
     * @date 2019-08-29 10:03 AM
     * @since 2.0.0
     **/
    List<CmsTemplate> listAll();
}
