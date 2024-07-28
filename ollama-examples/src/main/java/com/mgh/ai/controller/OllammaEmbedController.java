package com.mgh.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController

public class OllammaEmbedController {


    private final VectorStore vectorStore;

    private final ChatClient chatClient;



    public OllammaEmbedController(VectorStore vectorStore, ChatClient chatClient) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClient;
    }

    @GetMapping("/rag")
    public String getEmbed(@RequestParam String query) {
        SearchRequest searchRequest = SearchRequest.query(query).withTopK(1);
        List<Document> documents = vectorStore.similaritySearch(searchRequest);
        String releaventInfo = documents.stream().map(Document::getContent).collect(Collectors.joining(System.lineSeparator()));
        var systemPrompt = """
                You will be working as project guide. You will provide the requested information. Answer precisely to the question, do not add addition information.
                Use the following information to answer the question:
                {information}
                """;
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemPrompt);
        Message information = systemPromptTemplate.createMessage(Map.of("information", releaventInfo));

        var userMessage = """
                Tell me about the {query}
                """;
        PromptTemplate template = new PromptTemplate(userMessage);
        UserMessage userMeesage1 = new UserMessage(template.create(Map.of("query", query)).getContents());
        return chatClient.prompt(new Prompt(List.of(userMeesage1,information))).call().content();
    }
}
