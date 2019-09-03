package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator.
 */
@Mapper
public interface TeachplanMapper {

   /**
    * 根据课程id获取课程计划
    *
    * @author guoxing
    * @date 2019-09-03 4:49 PM
    * @since 2.0.0
    **/
   List<Teachplan> listByCourseId(String courseId);

   /**
    * 根据课程id获取课程计划,要求 parentId 为 0, 为最高节点
    *
    * @author guoxing
    * @date 2019-09-03 5:05 PM
    * @since 2.0.0
    **/
   Teachplan getMainTeachplanByCourseId(@Param("courseId") String courseId);

   /**
    * 根据parentId 获取 子节点
    *
    * @author guoxing
    * @date 2019-09-03 5:25 PM
    * @since 2.0.0
    **/
   List<Teachplan> listByParentId(@Param("parentId") String id);
}
