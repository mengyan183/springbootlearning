package com.xuecheng.manage_media.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Configuration
@Data
public class SystemConfig implements Serializable {
    private static final long serialVersionUID = 1999162427359974928L;

    @Value("${xc-service-manage-media.upload-location}")
    private String uploadFileLocation;

}
