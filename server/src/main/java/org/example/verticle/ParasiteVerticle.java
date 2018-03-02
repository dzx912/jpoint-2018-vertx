package org.example.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;

public class ParasiteVerticle extends AbstractVerticle {
    @Override
    public void start() {
        vertx.eventBus().consumer("longOperation", this::correctBlockingCode);
        vertx.eventBus().consumer("parasite", this::unCorrectBlockingCode);
    }

    private void correctBlockingCode(Message<String> message) {
        vertx.executeBlocking(future -> {
            sleep(10000L);
            future.complete("ok");
        }, res -> System.out.println("The result is: " + res.result()));
    }

    private void unCorrectBlockingCode(Message<String> message) {
        sleep(10000L);
    }

    private void sleep(Long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
