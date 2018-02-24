package org.example;

import org.example.chat.Cluster;
import org.example.verticle.LoggerVerticle;

import java.io.IOException;

public class Starter {

    public static void main(String[] args) throws IOException {
        new Cluster("jbreak-chat",
                new LoggerVerticle())
                .run();
    }
}
