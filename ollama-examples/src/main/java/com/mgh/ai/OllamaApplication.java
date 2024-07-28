package com.mgh.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class OllamaApplication {
    public static void main(String[] args) {

        SpringApplication build = new SpringApplicationBuilder().sources(OllamaApplication.class)
                .web(WebApplicationType.SERVLET)
                .build(args);
        build.run(args);
    }
}