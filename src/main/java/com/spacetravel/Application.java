package com.spacetravel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application extends SpringApplication implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Space navigator service started successfully");
    }
}
