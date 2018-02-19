package org.example;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.ignite.IgniteClusterManager;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.kubernetes.TcpDiscoveryKubernetesIpFinder;
import org.example.verticle.*;

public class Starter {
    public static void main(String[] args) {
        setupCluster();
    }

    private static void setupCluster() {
        ClusterManager clusterManager = new IgniteClusterManager(getIgniteConfiguration());

        VertxOptions options = new VertxOptions().setClusterManager(clusterManager);
        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                Vertx vertx = res.result();

                deploy(vertx);
            } else {
                System.out.println("Can't create cluster.");
                System.exit(1);
            }
        });
    }

    private static IgniteConfiguration getIgniteConfiguration() {
        TcpDiscoverySpi spi = new TcpDiscoverySpi();

        TcpDiscoveryKubernetesIpFinder ipFinder = new TcpDiscoveryKubernetesIpFinder();

        ipFinder.setServiceName("jbreak-chat");

        spi.setIpFinder(ipFinder);
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setDiscoverySpi(spi);
        return cfg;
    }

    private static void deploy(Vertx vertx) {
        vertx.deployVerticle(new WsApiVerticle());
        vertx.deployVerticle(new RestApiVerticle());
        vertx.deployVerticle(new RouterVerticle());
        vertx.deployVerticle(new LoggerVerticle());
        vertx.deployVerticle(new MongoDbVerticle());
    }
}
