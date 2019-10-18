package com.xuecheng.manage_course.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * SystemConfig
 *
 * @author guoxing
 * @date 10/18/2019 4:01 PM
 * @since 2.0.0
 **/
@Configuration
@Data
public class SystemConfig implements Serializable {
    private static final long serialVersionUID = 752129140946418852L;
    @Value("${course‐publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course‐publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course‐publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course‐publish.siteId}")
    private String publish_siteId;
    @Value("${course‐publish.templateId}")
    private String publish_templateId;
    @Value("${course‐publish.previewUrl}")
    private String previewUrl;
}
