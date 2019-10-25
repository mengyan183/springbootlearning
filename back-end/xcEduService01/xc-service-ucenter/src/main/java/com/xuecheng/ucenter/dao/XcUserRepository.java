package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * XcUserRepository
 *
 * @author guoxing
 * @date 10/25/2019 11:22 AM
 * @since 2.0.0
 **/
public interface XcUserRepository extends JpaRepository<XcUser, String> {

    /**
     * 根据用户名查询用户
     *
     * @param userName 用户名
     * @return 用户
     */
    XcUser findByUsername(String userName);

}
