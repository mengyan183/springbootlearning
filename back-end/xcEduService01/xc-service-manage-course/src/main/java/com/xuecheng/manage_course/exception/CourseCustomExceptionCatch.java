package com.xuecheng.manage_course.exception;

import com.xuecheng.framework.exception.ExceptionCatch;
import com.xuecheng.framework.model.response.CommonCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 课程管理自定义异常处理器
 */
@ControllerAdvice
public class CourseCustomExceptionCatch extends ExceptionCatch {

    static {
        //定义异常类型所对应的错误代码 ,添加未授权异常处理
        builder.put(AccessDeniedException.class, CommonCode.UNAUTHORISE);
    }
}
