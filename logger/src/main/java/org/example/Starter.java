package org.example;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.ignite.IgniteClusterManager;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.kubernetes.TcpDiscoveryKubernetesIpFinder;
import org.example.verticle.LoggerVerticle;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Starter {

    public static void main(String[] args) throws IOException {
        setupCluster();
    }

    private static void setupCluster() throws IOException {

        ClusterManager clusterManager = new IgniteClusterManager(getIgniteConfiguration());

        List<Inet4Address> inetAddresses = getNonLoopbackLocalIPv4Addresses();
        String publicClusterHost = inetAddresses.stream().map(Inet4Address::getHostAddress).findFirst().get();
        System.out.println("publicClusterHost: " + publicClusterHost);


        VertxOptions options = new VertxOptions()
                .setClustered(true)
                .setClusterManager(clusterManager)
                .setClusterHost(publicClusterHost);

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
        vertx.deployVerticle(new LoggerVerticle());
    }

    private static List<Inet4Address> getNonLoopbackLocalIPv4Addresses() throws IOException {
        List<Inet4Address> localAddresses = new ArrayList<>();

        Enumeration en = NetworkInterface.getNetworkInterfaces();
        while (en.hasMoreElements()) {
            NetworkInterface i = (NetworkInterface) en.nextElement();
            for (Enumeration en2 = i.getInetAddresses(); en2.hasMoreElements(); ) {
                InetAddress addr = (InetAddress) en2.nextElement();
                if (!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
                    localAddresses.add((Inet4Address) addr);
                }
            }
        }
        return localAddresses;
    }
}
