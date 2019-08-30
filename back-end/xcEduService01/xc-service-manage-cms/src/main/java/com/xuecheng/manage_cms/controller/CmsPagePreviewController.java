/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */
package com.xuecheng.manage_cms.controller;

import com.xuecheng.manage_cms.service.PageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * CmsPagePreviewController
 *
 * @author guoxing
 * @date 8/30/2019 4:10 PM
 * @since 2.0.0
 **/
@Controller
public class CmsPagePreviewController {
    @Autowired
    private PageService pageService;

    /**
     * 静态化页面预览
     *
     * @author guoxing
     * @date 2019-08-30 4:13 PM
     * @since 2.0.0
     **/
    @RequestMapping(value = "/cms/preview/{pageId}", method = RequestMethod.GET)
    public void previewPageByPageId(@PathVariable("pageId") String pageId, HttpServletResponse httpServletResponse) {
        try {
            String pageHtmlByPageId = pageService.getPageHtmlByPageId(pageId);
            if (StringUtils.isNotBlank(pageHtmlByPageId)) {
                ServletOutputStream outputStream = httpServletResponse.getOutputStream();
                outputStream.write(pageHtmlByPageId.getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
