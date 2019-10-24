package com.xuecheng.auth.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * SystemConfig
 *
 * @author guoxing
 * @date 10/24/2019 6:40 PM
 * @since 2.0.0
 **/
@Data
@Configuration
public class SystemConfig implements Serializable {
    /**
     * 认证服务客户端id
     */
    @Value("{auth.clientId}")
    private String authClientId;

    /**
     * 认证服务客户端secret
     */
    @Value("{auth.clientSecret}")
    private String authClientSecret;

    /**
     * token过期时间
     */
    @Value("{auth.tokenValiditySeconds}")
    private Long authTokenValiditySeconds;
    /**
     * cookie domain
     */
    @Value("{auth.cookieDomain}")
    private String cookieDomain;

    /**
     * cookie过期时间
     */
    @Value("{auth.cookieMaxAge}")
    private Long cookieMaxAge;
}
