package org.example.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import org.example.data.Data;

public class RouterVerticle extends AbstractVerticle {
    @Override
    public void start() {
        vertx.eventBus().localConsumer("router", this::router);
    }

    private void router(Message<String> message) {
        if (message.body() != null) {
            System.out.println("Router message: " + message.body());
            Data data = Json.decodeValue(message.body(), Data.class);
            System.out.println(data);
            vertx.eventBus().publish("/token/" + data.getAddress(), message.body());
        } else {
            System.out.println("ERROR Router data is empty");
        }
    }
}
