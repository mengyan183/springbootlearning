package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * XcCompanyUserRepository
 *
 * @author guoxing
 * @date 10/25/2019 11:23 AM
 * @since 2.0.0
 **/
public interface XcCompanyUserRepository extends JpaRepository<XcCompanyUser, String> {
    /**
     * 根据用户id获取该用户所属的公司id
     *
     * @param userId 用户id
     * @return 公司id信息
     */
    XcCompanyUser findByUserId(String userId);
}
