package org.example.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.ServerWebSocket;

public class WsApiVerticle extends AbstractVerticle {
    @Override
    public void start() {
        vertx.createHttpServer()
                .websocketHandler(this::createWebSocketServer)
                .listen(8080);
    }

    private void createWebSocketServer(ServerWebSocket wsServer) {
        System.out.println("Create WebSocket: " + wsServer.path());
        wsServer.frameHandler(wsFrame -> {
            System.out.println(wsFrame.textData());
            vertx.eventBus().publish("router", wsFrame.textData());
        });

        MessageConsumer<String> consumerSendMessage = vertx.eventBus().<String>consumer(wsServer.path(), data -> {
            wsServer.writeFinalTextFrame(data.body());
            data.reply("ok");
        });

        wsServer.closeHandler(aVoid -> consumerSendMessage.unregister());
    }


    private void wsConsumer(ServerWebSocket wsServer) {
        MessageConsumer<String> consumerSendMessage =
                vertx.eventBus().<String>consumer(wsServer.path(), data -> {
                    wsServer.writeFinalTextFrame(data.body());
                    data.reply("ok");
                });
        System.out.println(wsServer.path());

        // Снимаем обработчик, после закрытия WebSocket'а
        wsServer.closeHandler(aVoid -> consumerSendMessage.unregister());
    }
}
