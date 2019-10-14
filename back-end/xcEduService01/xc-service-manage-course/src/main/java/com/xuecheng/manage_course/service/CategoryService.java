package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.Category;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.dao.CategoryMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * CategoryService
 *
 * @author guoxing
 * @date 10/14/2019 11:38 AM
 * @since 2.0.0
 **/
@Service
public class CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    //查询分类
    public CategoryNode findList() {
        // 纯sql 查询 有 局限性
//        return categoryMapper.selectList();

        // 使用 递归进行 查询
        String parentId = "0";
        List<CategoryNode> categoryNodes = listByParentId(parentId);
        if (CollectionUtils.isEmpty(categoryNodes)) {
            return null;
        } else {
            return categoryNodes.get(0);
        }
    }


    private List<CategoryNode> listByParentId(String parentId) {
        List<Category> categoryList = categoryMapper.selectByParentId(parentId);
        if (CollectionUtils.isEmpty(categoryList)) {
            return null;
        }
        List<CategoryNode> categoryNodes = new ArrayList<>(categoryList.size());
        for (Category category : categoryList) {
            if (category != null) {
                CategoryNode categoryNode = new CategoryNode();
                BeanUtils.copyProperties(category, categoryNode);
                categoryNode.setChildren(listByParentId(category.getId()));
                categoryNodes.add(categoryNode);
            }
        }
        return categoryNodes;
    }
}
