package org.example;

import io.vertx.core.Vertx;
import org.example.verticle.*;

public class Starter {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(new WsApiVerticle());
        vertx.deployVerticle(new RestApiVerticle());
        vertx.deployVerticle(new RouterVerticle());
        vertx.deployVerticle(new LoggerVerticle());
        vertx.deployVerticle(new MongoDbVerticle());
    }
}
