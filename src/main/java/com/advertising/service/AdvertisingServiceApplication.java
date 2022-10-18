package com.advertising.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.advertising.service"})
public class AdvertisingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdvertisingServiceApplication.class, args);
    }
}
