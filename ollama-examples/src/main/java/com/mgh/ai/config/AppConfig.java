package com.mgh.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class AppConfig {

    @Bean
    ChatClient chatClient(ChatClient.Builder chatClientBuilder){
        return chatClientBuilder.build();
    }
}
