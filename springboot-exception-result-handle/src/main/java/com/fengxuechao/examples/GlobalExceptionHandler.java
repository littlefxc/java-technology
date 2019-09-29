package com.fengxuechao.examples;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @author fengxuechao
 * @version 0.1
 * @date 2019/9/29
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 自定义异常捕获
     *
     * @param e
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public ResultBean handle(CustomException e) {
        return ResultBean.error(e.getCode(), e.getMessage());
    }
}
