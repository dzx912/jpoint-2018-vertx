package org.example.handler;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.ServerWebSocket;

public class SendMessageHandler implements Handler<Message<String>> {
    private ServerWebSocket webSocket;

    public SendMessageHandler(ServerWebSocket webSocket) {
        this.webSocket = webSocket;
    }

    @Override
    public void handle(Message<String> data) {
        webSocket.writeFinalTextFrame(data.body());
        data.reply("ok");
    }
}
