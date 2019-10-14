package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Category;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * CategoryMapper
 *
 * @author guoxing
 * @date 10/14/2019 11:38 AM
 * @since 2.0.0
 **/
@Mapper
public interface CategoryMapper {

    /**
     * 子节点查询
     * @return
     */
    CategoryNode selectList();

    /**
     * 通过 父节点查询子节点
     * @param parentId
     * @return
     */
    List<Category> selectByParentId(String parentId);
}
