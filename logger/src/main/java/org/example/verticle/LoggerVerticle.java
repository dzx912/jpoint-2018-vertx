package org.example.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;

public class LoggerVerticle extends AbstractVerticle {
    @Override
    public void start() {
        vertx.eventBus().consumer("router", this::log);
    }

    private void log(Message<String> message) {
        System.out.println("Send message: " + message.body());
    }
}
