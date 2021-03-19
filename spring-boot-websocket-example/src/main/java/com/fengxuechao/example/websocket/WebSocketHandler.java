/*
 * 版权所有: 爱WiFi无线运营中心
 * 创建日期: 2021/3/18
 * 创建作者: 冯雪超
 * 文件名称: WebSocketHandler.java
 * 版本: v1.0
 * 功能:
 * 修改记录：
 */
package com.fengxuechao.example.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * @author fengxuechao
 * @date 2021/3/18
 */
@Component
public class WebSocketHandler extends AbstractWebSocketHandler {

    /**
     * 连接成功时候，会触发页面上onopen方法
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        System.out.println("连接建立");
    }

    /**
     * js调用websocket.send()时候，会调用该方法
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
        System.out.println("处理消息");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        System.out.println("处理文本消息");
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        super.handleBinaryMessage(session, message);
        System.out.println("处理二进制消息");
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        super.handlePongMessage(session, message);
        System.out.println("处理Pong消息");
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        System.out.println("处理异常");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        System.out.println("连接关闭");
    }
}
