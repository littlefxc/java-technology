package com.fengxuechao.examples.springrest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/4/18
 * @title ResourceNotFoundException
 * @project_name spring-rest-examples
 * @description TODO
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
}
