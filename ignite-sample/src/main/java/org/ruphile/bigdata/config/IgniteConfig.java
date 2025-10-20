package org.ruphile.bigdata.config;

import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class IgniteConfig {
    @Bean
    public IgniteConfiguration igniteConfiguration() {
        // Preparing IgniteConfiguration using Java APIs
        IgniteConfiguration cfg = new IgniteConfiguration();
        // The node will be started as a client node.
        cfg.setClientMode(true);
        // Classes of custom Java logic will be transferred over the wire from this app.
        cfg.setPeerClassLoadingEnabled(true);
        // Setting up an IP Finder to ensure the client can locate the servers.
        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(Collections.singletonList("localhost:47500"));
        cfg.setDiscoverySpi(new TcpDiscoverySpi().setIpFinder(ipFinder));
        cfg.setIgniteInstanceName("ignite-db-sync");

        // Starting the node
        return cfg;
    }


    /*@Bean
    public ClientConfiguration clientConfiguration() {
        // If you provide a whole ClientConfiguration bean then configuration properties will not be used.
        ClientConfiguration cfg = new ClientConfiguration();
        cfg.setAddresses("localhost:10800").setPartitionAwarenessEnabled(true);
        return cfg;
    }*/
}
