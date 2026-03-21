package com.lab.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 🔒 JWT Auth Service
 *
 * Concept: Stateless authentication across microservices.
 * This service issues JWT tokens. Other services validate them
 * without calling back to this service — fully stateless.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class JwtAuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(JwtAuthServiceApplication.class, args);
    }
}
