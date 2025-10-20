package org.ruphile.bigdata;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class ThickClient {
    /**
     * Main method of the application.
     * @param args Arguments.
     */
    public static void main(String[] args) {
        System.out.println("ServiceWithIgniteClient.run");

        SpringApplication.run(ThickClient.class);
    }
}
