package com.mgh.ai.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class OllamaChatController {
    public record Response(String query, String response) {

    }

    @Autowired
    private ChatClient ollamaChatClient;

    @GetMapping("/chat")
    public Response getChatReply(@RequestParam("query") String query) {
        var response = ollamaChatClient.prompt().user(query).call().content();
        return new Response(query, response);
    }

    @GetMapping("/prompt")
    public Response getChatUsingPromptTemplate(@RequestParam("query") String query){
        PromptTemplate template = new PromptTemplate("Tell me capital city of a {country}");
        Prompt prompt = template.create(Map.of("country", query));
        String content = ollamaChatClient.prompt(prompt).call().content();
        return new Response(query, content);
    }


    @GetMapping("/promptTemplate")
    public Response getChatUsingPrompt(@RequestParam("query") String query){
        SystemMessage systemMessage = new SystemMessage("You are a school teacher and very politely  answer the children question. Do not include additional information.");
        PromptTemplate template = new PromptTemplate("Tell me capital city of a {country}");
        Prompt prompt = new Prompt(List.of(systemMessage,template.createMessage(Map.of("country", query))));
        String content = ollamaChatClient.prompt(prompt).call().content();
        return new Response(query, content);
    }


    @GetMapping("/parser")
    public Data getOutputParserResult(@RequestParam("scientist") String scientist){
        var outputParser = new BeanOutputConverter<Data>(Data.class);

        String userMessage =
                """
                Generate the inventions of  the scientist {scientist}.
                {format}
                """;

        PromptTemplate promptTemplate = new PromptTemplate(userMessage, Map.of("scientist", scientist, "format", outputParser.getFormat() ));
        Prompt prompt = promptTemplate.create();
        Generation generation = ollamaChatClient.prompt(prompt).call().chatResponse().getResult();
        System.out.printf(generation.getOutput().getContent());
        return outputParser.convert(generation.getOutput().getContent());
    }

    record Data (String scientist, List<String> inventions){};
}
