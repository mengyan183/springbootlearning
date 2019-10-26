package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 用户权限菜单列表
 */
@Mapper
public interface XcMenuMapper {

    /**
     * 根据用户uid查询该用户所有可以操作的菜单列表
     *
     * @param id
     * @return
     */
    List<XcMenu> selectPermissionByUserId(@Param("id") String id);
}
