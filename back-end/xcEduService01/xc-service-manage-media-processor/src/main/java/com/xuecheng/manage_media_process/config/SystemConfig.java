package com.xuecheng.manage_media_process.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Configuration
@Data
public class SystemConfig implements Serializable {

    private static final long serialVersionUID = -2941580742105452371L;
    /**
     * 视频文件上传地址
     */
    @Value("${xc-service-manage-media.video-location}")
    private String uploadVideoLocation;
    /**
     * ffmpeg 所在文件地址
     */
    @Value("${xc-service-manage-media.ffmpeg-path}")
    private String ffmpegPath;
}
