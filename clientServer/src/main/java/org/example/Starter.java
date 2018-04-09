package org.example;

import io.vertx.core.Vertx;
import org.example.verticle.ClientServerVerticle;

public class Starter {
    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new ClientServerVerticle());
    }
}
