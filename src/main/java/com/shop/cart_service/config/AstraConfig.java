package com.shop.cart_service.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;

@Configuration
public class AstraConfig {

    @Bean
    public CqlSession cassandraSession() {
        // We use the absolute path here so IntelliJ never gets confused about which folder to look in.
        return CqlSession.builder()
                .withCloudSecureConnectBundle(Paths.get("C:\\Users\\sampr\\Desktop\\myproj\\cart-service\\cart-service\\src\\main\\resources\\secure-connect-bundle.zip"))
                .withAuthCredentials(
                        "YOUR_CLIENT_ID_HERE",
                        "YOUR_CLIENT_SECRET_HERE")
                .withKeyspace("shopping_cart")
                .build();
    }
}