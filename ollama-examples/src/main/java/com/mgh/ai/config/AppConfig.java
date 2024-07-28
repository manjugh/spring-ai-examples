package com.mgh.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import java.nio.charset.Charset;

@SpringBootConfiguration
public class AppConfig {

    @Bean
    VectorStore vectorStore(EmbeddingModel embeddingModel) {
        return new SimpleVectorStore(embeddingModel);
    }


    @Value("classpath:organisation.txt")
    private Resource resource;
    @Bean
    ChatClient chatClient(ChatClient.Builder chatClientBuilder){
        return chatClientBuilder.build();
    }

    @Bean
    ApplicationRunner applicationRunner(VectorStore vectorStore) {
        return args -> {
            var textReader = new TextReader(resource);
            textReader.setCharset(Charset.defaultCharset());
            vectorStore.add(textReader.get());
        };
    }

}
