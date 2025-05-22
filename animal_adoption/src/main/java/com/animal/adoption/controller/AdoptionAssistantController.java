package com.animal.adoption.controller;


import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@ResponseBody
public class AdoptionAssistantController {

    public static final String GLOBAL_PROMPT =
            """
                You are a dog adoption assistant for 'hiii5', operating across India.
                Provide helpful answers based on available dog data.
                If a dog is not found, respond politely suggesting no availability.
            """;

    private final ChatClient router ;
    private final Map<String , ChatClient> delegates ;
    private final DefaultListableBeanFactory beanFactory ;
    private final Map<String , PromptChatMemoryAdvisor> memory = new ConcurrentHashMap<>();

    public AdoptionAssistantController (ChatClient.Builder ai,
                                        Map<String , ChatClient> clientMap,
                                        DefaultListableBeanFactory beanFactory,
                                        McpSyncClient client,
                                        QuestionAnswerAdvisor questionAnswerAdvisor ) {

        this.beanFactory = beanFactory ;
        this.delegates = clientMap ;

        var system = """
				You're a request router.
				When a user sends a message, select the most appropriate category from the ones listed below, based on their intent.
				Respond with only the category name.
				""";

        for (var beanName : clientMap.keySet()){
            final var beanDefinitionDescription = getBeanDefinitionDescription( beanName);
            system += "\n\n" + beanName + " : " + beanDefinitionDescription + "\n\n" ;
        }

        this.router = ai
                .defaultAdvisors(questionAnswerAdvisor)
                .defaultSystem(system)
                .build();
    }

    private String getBeanDefinitionDescription(String beanName) {
        var beanDefinition = beanFactory.getBeanDefinition(beanName);
        return beanDefinition.getDescription();
    }

    @GetMapping("/{user}/inquire")
    String inquire(@PathVariable String user, @RequestParam String question) {

        var memAdvisor = memory.computeIfAbsent(user, u -> {
            ChatMemory chatMemory = MessageWindowChatMemory.builder()
                    .maxMessages(100)
                    .build();
            return PromptChatMemoryAdvisor.builder(chatMemory).build();
        });

        var resolvedChatClient = this.router
                .prompt()
                .user(question)
                .call()
                .content() ;

		/*
		return router.prompt()
				.advisors(memAdvisor)
				.user(question)
				.system(system)
				.call()
				.content();*/

        return delegates
                .get(resolvedChatClient)
                .prompt()
                .advisors(memAdvisor)
                .user(question)
                .system(GLOBAL_PROMPT + getBeanDefinitionDescription(resolvedChatClient))
                .call()
                .content();
    }
}

