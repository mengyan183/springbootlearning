package com.xuecheng.order.dao;

import com.xuecheng.framework.domain.task.XcTaskHis;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 任务dao
 */
public interface XcTaskHisRepository extends JpaRepository<XcTaskHis, String> {
}
