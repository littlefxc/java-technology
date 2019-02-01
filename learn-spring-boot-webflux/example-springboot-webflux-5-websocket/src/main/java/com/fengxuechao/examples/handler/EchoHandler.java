package com.fengxuechao.examples.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

/**
 * @author fengxuechao
 * @date 2019/1/11
 **/
@Component
public class EchoHandler implements WebSocketHandler {

    @Override
    public Mono<Void> handle(final WebSocketSession session) {
        return session.send(
                session.receive()
                        .map(msg -> {
                            String s = "服务端返回：小明， -> " + msg.getPayloadAsText();
                            System.out.println(s);
                            return session.textMessage(s);
                        }));
    }
}
