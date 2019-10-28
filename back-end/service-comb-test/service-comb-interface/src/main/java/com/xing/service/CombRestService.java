package com.xing.service;

/**
 * CombRestService
 * rest 微服务接口定义
 *
 * @author guoxing
 * @date 10/28/2019 2:45 PM
 * @since 2.0.0
 **/
public interface CombRestService {
    /**
     * 首次调用rest
     *
     * @param name
     * @return
     */
    String sayRest(String name);
}
