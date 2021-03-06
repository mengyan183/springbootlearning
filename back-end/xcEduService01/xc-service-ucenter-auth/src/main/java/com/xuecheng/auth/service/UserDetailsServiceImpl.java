package com.xuecheng.auth.service;

import com.xuecheng.auth.client.UserServiceClient;
import com.xuecheng.framework.domain.ucenter.XcMenu;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private UserServiceClient userServiceClient;

    /**
     * 实现密码登录授权认证
     * 认证的步骤为:
     *  1: 首先进行客户端认证, 获取配置文件中的auth.clientId 是否和当前请求中携带的clientid 是否一致,如果不一致直接返回AccessDeniedException Access is denied;clientId认证通过后,匹配clientSecret是否一致;client认证通过后,返回当前认证服务器客户端用户,并把数据注入spring容器,实现身份授权;
     *  2:如果当前请求中grandType 为password 则证明要求请求参数中需要传递userName和password,然后进行自定义用户名密码校验;下发令牌的过程为认证服务使用jwt对当前请求中携带的用户名密码进行私钥认证;校验通过后返回 令牌
     *
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //取出身份，如果身份为空说明没有认证
        // 1: 进行clientid认证,校验请求传递的clientId是否一致
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //没有认证统一采用httpbasic认证，httpbasic中存储了client_id和client_secret，开始认证client_id和client_secret
        if(authentication==null){
            // 2: 根据clientId 校验clientSecret是否一致
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(username);
            if(clientDetails!=null){
                //密码
                String clientSecret = clientDetails.getClientSecret();
                return new User(username,clientSecret,AuthorityUtils.commaSeparatedStringToAuthorityList(""));
            }
        }
        if (StringUtils.isEmpty(username)) {
            log.error("用户名不能为空");
            return null;
        }
        // 身份认证通过返回 用户token信息
        XcUserExt userext = userServiceClient.getUserext(username);
        if(userext == null){
            // 返回空给spring security 表示该用户不存在
            log.error("未找到该用户;userName:{}", username);
            return null;
        }
        //取出正确密码（hash值）
        String password = userext.getPassword();
        //用户权限，这里暂时使用静态数据，最终会从数据库读取
        //从数据库获取权限
        List<XcMenu> permissions = userext.getPermissions();
        String collect = permissions.stream().map(XcMenu::getCode).collect(Collectors.joining(","));
        UserJwt userDetails = new UserJwt(username,
                password,
                AuthorityUtils.commaSeparatedStringToAuthorityList(collect));
        userDetails.setId(userext.getId());
        userDetails.setUtype(userext.getUtype());//用户类型
        userDetails.setCompanyId(userext.getCompanyId());//所属企业
        userDetails.setName(userext.getName());//用户名称
        userDetails.setUserpic(userext.getUserpic());//用户头像
       /* UserDetails userDetails = new org.springframework.security.core.userdetails.User(username,
                password,
                AuthorityUtils.commaSeparatedStringToAuthorityList(""));*/
//                AuthorityUtils.createAuthorityList("course_get_baseinfo","course_get_list"));
        return userDetails;
    }
}
