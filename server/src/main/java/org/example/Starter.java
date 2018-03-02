package org.example;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import org.example.chat.Cluster;
import org.example.verticle.ParasiteVerticle;
import org.example.verticle.RestServerVerticle;
import org.example.verticle.RouterVerticle;
import org.example.verticle.WsServerVerticle;

import java.util.Arrays;
import java.util.List;

public class Starter {

    public static void main(String[] args) {
        List<Verticle> verticles = Arrays.asList(
                new RouterVerticle(),
                new ParasiteVerticle(),
                new WsServerVerticle(),
                new RestServerVerticle()
        );

//        new Cluster("jbreak-chat", verticles)
//                .run();

        deploy(Vertx.vertx(), verticles);
    }

    private static void deploy(Vertx vertx, List<Verticle> verticles) {
        verticles.forEach(vertx::deployVerticle);
    }
}
