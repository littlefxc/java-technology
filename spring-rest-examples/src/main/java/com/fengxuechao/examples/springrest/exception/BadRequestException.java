package com.fengxuechao.examples.springrest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/4/18
 * @title BadRequestException
 * @project_name spring-rest-examples
 * @description TODO
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
}
