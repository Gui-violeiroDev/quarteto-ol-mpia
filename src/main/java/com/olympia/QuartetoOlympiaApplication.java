package com.olympia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class QuartetoOlympiaApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuartetoOlympiaApplication.class, args);
    }
}
