package org.example;

import org.example.chat.Cluster;
import org.example.verticle.RouterVerticle;

public class Starter {

    public static void main(String[] args) {
        new Cluster(new RouterVerticle())
                .run();
    }
}
