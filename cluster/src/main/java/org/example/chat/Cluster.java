package org.example.chat;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.ignite.IgniteClusterManager;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.kubernetes.TcpDiscoveryKubernetesIpFinder;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class Cluster {
    private final static String SERVICE_NAME = "chat";

    private final List<Verticle> verticles;
    private final String serviceName;

    public Cluster(Verticle verticle) {
        this(SERVICE_NAME, verticle);
    }

    public Cluster(String serviceName, Verticle verticle) {
        this(serviceName, Collections.singletonList(verticle));
    }

    public Cluster(List<Verticle> verticles) {
        this(SERVICE_NAME, verticles);
    }

    public Cluster(String serviceName, List<Verticle> verticles) {
        this.serviceName = serviceName;
        this.verticles = verticles;
    }

    public void run() {
        try {
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
        } catch (IOException e) {
            System.out.println("Can't find IP address: " + e.toString());
            System.exit(2);
        }
    }

    private IgniteConfiguration getIgniteConfiguration() {
        TcpDiscoverySpi spi = new TcpDiscoverySpi();

        TcpDiscoveryKubernetesIpFinder ipFinder = new TcpDiscoveryKubernetesIpFinder();

        ipFinder.setServiceName(serviceName);

        spi.setIpFinder(ipFinder);
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setDiscoverySpi(spi);
        return cfg;
    }

    private List<Inet4Address> getNonLoopbackLocalIPv4Addresses() throws IOException {
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

    private void deploy(Vertx vertx) {
        verticles.forEach(vertx::deployVerticle);
    }
}
