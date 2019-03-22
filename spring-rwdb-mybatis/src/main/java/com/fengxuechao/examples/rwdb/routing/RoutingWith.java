package com.fengxuechao.examples.rwdb.routing;

import com.fengxuechao.examples.rwdb.config.RoutingType;

import java.lang.annotation.*;

/**
 * @author fengxuechao
 * @date 2019/3/22
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RoutingWith {
    RoutingType value() default RoutingType.MASTER;
}
