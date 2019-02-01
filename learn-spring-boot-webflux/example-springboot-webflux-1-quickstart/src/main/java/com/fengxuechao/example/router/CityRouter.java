package com.fengxuechao.example.router;

import com.fengxuechao.example.handler.CityHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * 路由类
 *
 * @date 12/14/2018
 * @author fengxuechao
 */
@Configuration
public class CityRouter {

    /**
     * RouterFunctions 对请求路由处理类，即将请求路由到处理器，这里将一个 GET 请求 /hello 路由到处理器 cityHandler 的 helloCity 方法上。
     * 跟 Spring MVC 模式下的 HandleMapping 的作用类似。
     * RouterFunctions.route(RequestPredicate, HandlerFunction) 方法，对应的入参是请求参数和处理函数，如果请求匹配，就调用对应的处理器函数。
     *
     * @param cityHandler
     * @return
     */
    @Bean
    public RouterFunction<ServerResponse> routeCity(CityHandler cityHandler) {
        return RouterFunctions
                .route(RequestPredicates
                        .GET("/hello")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), cityHandler::helloCity);
    }

}