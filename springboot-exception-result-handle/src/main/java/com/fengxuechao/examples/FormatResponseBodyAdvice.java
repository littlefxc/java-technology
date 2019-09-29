package com.fengxuechao.examples;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 正常结果统一返回处理
 *
 * @author fengxuechao
 * @version 0.1
 * @date 2019/9/27
 */
@Slf4j
@ControllerAdvice
public class FormatResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 决定是否要统一结果处理
     *
     * @param methodParameter
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
//        return methodParameter.getDeclaringClass().equals(HelloController.class);
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object data, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        return ResultBean.ok(data);
    }
}
