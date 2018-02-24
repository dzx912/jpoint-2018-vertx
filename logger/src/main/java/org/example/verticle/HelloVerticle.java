package org.example.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;

public class HelloVerticle extends AbstractVerticle {

    @Override
    public void start() {
        vertx.eventBus().consumer("hello", this::hello);

        vertx.eventBus().publish("hello", "My first message");
    }

    private void hello(Message<String> data) {
        String message = data.body();
        System.out.println(message);
    }
}
