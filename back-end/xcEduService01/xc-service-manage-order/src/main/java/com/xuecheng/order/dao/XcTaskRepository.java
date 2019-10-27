package com.xuecheng.order.dao;

import com.xuecheng.framework.domain.task.XcTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

/**
 * 任务dao
 */
public interface XcTaskRepository extends JpaRepository<XcTask, String> {
    //分页查询某个时间之前的前n条记录
    Page<XcTask> findByUpdateTimeBefore(Pageable pageable, Date updateTime);

    /**
     *自定义sql语句 更新任务更新时间
     * @param id
     * @return
     */
    @Modifying
    @Query("update XcTask set updateTime = current_timestamp where id = :id")
    int updateTaskTime(@Param("id") String id);
}
