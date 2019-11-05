package com.xuecheng.portalview.exception;

import com.xuecheng.framework.exception.ExceptionCatch;
import org.springframework.web.bind.annotation.ControllerAdvice;


/**
 * Created by admin on 2018/3/20.
 */
@ControllerAdvice
public class CustomExceptionCatch extends ExceptionCatch {

    static {
        //除了CustomException以外的异常类型及对应的错误代码在这里定义,，如果不定义则统一返回固定的错误信息
        //例子 ExceptionCatch.builder.put(AccessDeniedException.class, CommonCode.UNAUTHORISE);
    }
}
