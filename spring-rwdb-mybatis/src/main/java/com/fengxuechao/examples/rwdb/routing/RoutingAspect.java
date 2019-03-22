package com.fengxuechao.examples.rwdb.routing;

import com.fengxuechao.examples.rwdb.config.CustomerContextHolder;
import com.fengxuechao.examples.rwdb.config.CustomerType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 使用限制:受Servlet线程模型的局限，动态数据源不能在一个请求内设定后再修改，也就是@RoutingWith不能嵌套。此外，@RoutingWith和@Transactional混用时，要设定AOP的优先级。
 *
 * @author fengxuechao
 */
@Aspect
@Component
public class RoutingAspect {
    @Around("@annotation(routingWith)")
    public Object routingWithDataSource(ProceedingJoinPoint joinPoint, RoutingWith routingWith) throws Throwable {
        CustomerType customerType = routingWith.value();
        CustomerContextHolder.setCustomerType(customerType);
        return joinPoint.proceed();
    }
}