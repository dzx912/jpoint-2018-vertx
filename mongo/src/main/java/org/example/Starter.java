package org.example;

import org.example.chat.Cluster;
import org.example.verticle.MongoDbVerticle;

public class Starter {

    public static void main(String[] args) {
        new Cluster("jbreak-chat",
                new MongoDbVerticle())
                .run();
    }
}
