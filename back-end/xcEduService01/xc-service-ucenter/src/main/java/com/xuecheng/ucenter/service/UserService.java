package com.xuecheng.ucenter.service;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import com.xuecheng.framework.domain.ucenter.XcMenu;
import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.dao.XcCompanyUserRepository;
import com.xuecheng.ucenter.dao.XcMenuMapper;
import com.xuecheng.ucenter.dao.XcUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * UserService
 *
 * @author guoxing
 * @date 10/25/2019 11:21 AM
 * @since 2.0.0
 **/
@Service
@Slf4j
public class UserService {

    @Autowired
    private XcUserRepository xcUserRepository;

    @Autowired
    private XcCompanyUserRepository xcCompanyUserRepository;
    @Autowired
    private XcMenuMapper xcMenuMapper;


    /**
     * 获取用户信息
     *
     * @param username 用户名
     * @return 用户扩展信息
     */
    public XcUserExt getUserExt(String username) {
        if (StringUtils.isBlank(username)) {
            log.error("用户名不能为空");
            return null;
        }
        XcUser xcUser = xcUserRepository.findByUsername(username);
        if (xcUser == null) {
            log.error("用户不存在; userName:{}", username);
            return null;
        }
        XcUserExt xcUserExt = new XcUserExt();
        BeanUtils.copyProperties(xcUser, xcUserExt);
        // 查询用户公司信息
        XcCompanyUser xcCompanyUser = xcCompanyUserRepository.findByUserId(xcUser.getId());
        if (xcCompanyUser == null) {
            log.error("未找到该用户的所属公司信息;userId:{}", xcUser.getId());
        } else {
            xcUserExt.setCompanyId(xcCompanyUser.getCompanyId());
        }
        //该用户的相关权限信息
        List<XcMenu> xcMenus = xcMenuMapper.selectPermissionByUserId(xcUser.getId());
        xcMenus = CollectionUtils.isEmpty(xcMenus) ? Collections.emptyList() : xcMenus;
        xcUserExt.setPermissions(xcMenus);
        return xcUserExt;
    }
}
