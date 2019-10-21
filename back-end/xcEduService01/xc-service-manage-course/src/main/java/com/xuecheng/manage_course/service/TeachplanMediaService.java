package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.manage_course.dao.TeachplanMediaRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TeachplanMediaService
 *
 * @author guoxing
 * @date 10/21/2019 4:15 PM
 * @since 2.0.0
 **/
@Service
@Slf4j
public class TeachplanMediaService {

    @Autowired
    private TeachplanMediaRepository teachplanMediaRepository;

    /**
     * 获取课程计划媒资信息
     *
     * @param id
     * @return
     */
    public TeachplanMedia findById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return teachplanMediaRepository.findById(id).orElse(null);
    }
}
