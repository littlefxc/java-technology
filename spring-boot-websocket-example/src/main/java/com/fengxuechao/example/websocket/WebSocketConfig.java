/*
 * 版权所有: 爱WiFi无线运营中心
 * 创建日期: 2021/3/18
 * 创建作者: 冯雪超
 * 文件名称: WebSocketConfig.java
 * 版本: v1.0
 * 功能:
 * 修改记录：
 */
package com.fengxuechao.example.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author fengxuechao
 * @date 2021/3/18
 */
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private WebSocketHandler webSocketHandler;

    @Autowired
    private WebSocketHandshakeInterceptor handshakeInterceptor;

    /**
     * Register {@link WebSocketHandler WebSocketHandlers} including SockJS fallback options if desired.
     *
     * @param registry
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/data")
                .addInterceptors(handshakeInterceptor)
                .setAllowedOrigins("*");
    }
}
