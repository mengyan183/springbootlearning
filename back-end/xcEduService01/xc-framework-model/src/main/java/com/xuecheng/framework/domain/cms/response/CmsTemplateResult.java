/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.framework.domain.cms.response;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;

/**
 * @author guoxing
 * @date 2019-08-30 11:47 AM
 * @since 2.0.0
 **/
@Data
public class CmsTemplateResult extends ResponseResult {
    private CmsTemplate cmsTemplate;

    public CmsTemplateResult(ResultCode resultCode, CmsTemplate cmsTemplate) {
        super(resultCode);
        this.cmsTemplate = cmsTemplate;
    }
}
