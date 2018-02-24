package org.example;

import io.vertx.core.Verticle;
import org.example.chat.Cluster;
import org.example.verticle.RestServerVerticle;
import org.example.verticle.WsServerVerticle;

import java.util.Arrays;
import java.util.List;

public class Starter {

    public static void main(String[] args) {
        List<Verticle> verticles = Arrays.asList(
                new WsServerVerticle(),
                new RestServerVerticle());

        new Cluster("jbreak-chat", verticles)
                .run();
    }
}
