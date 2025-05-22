package com.animal.adoption.config;

import com.animal.adoption.controller.AdoptionAssistantController;
import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

@Configuration
public class ChatClientConfig {

    @Bean
    @Description("""
			Handles all post-adoption support services, such as dog training, therapy, obedience classes, and care consultations.\s
			Users may ask for available training sessions or how to take care of adopted dogs.
			Free training is available Monday to Friday, and Saturday mornings at hiii5 centers.
			""")
    public ChatClient postAdoptionFollowupCare(QuestionAnswerAdvisor advisor , ChatClient.Builder builder){
        return builder
                .defaultSystem(AdoptionAssistantController.GLOBAL_PROMPT)
                .defaultAdvisors(advisor)
                .build();
    }

    @Bean
    @Description("""
					Handles all user inquiries related to dog adoption and scheduling pickup appointments.
					This client integrates with the scheduling system using tool functions like `scheduler` for generating future pickup dates.
					Use this when the user asks when, how, or where to adopt or pick up a dog.
					""")
    public ChatClient adoptionAndScheduling (QuestionAnswerAdvisor advisor ,
                                      ChatClient.Builder builder ,
                                      McpSyncClient client) {

        return builder
                .defaultSystem(AdoptionAssistantController.GLOBAL_PROMPT)
                .defaultToolCallbacks(new SyncMcpToolCallbackProvider(client))
                .defaultAdvisors(advisor)
                .build();
    }


    @Bean
    @Description("""
			When user asks to confirm that he/she wants to book an appointment for adopting any dog, then invoke this.
			It has access to the scheduling subsystem via tools. This tool should be used to determine when a dog might be picked up or adopted from a given hiii5 location.
			IF YOU DON'T HAVE USERNAME , MOBILE NUMBER, AND THE LOCATION FROM WHERE THEY WANT TO PICK THE DOG THEN FIRST ASK FOR IT
			""")
    public ChatClient adoptionConfirmationProcess (QuestionAnswerAdvisor advisor ,
                                            ChatClient.Builder builder ,
                                            McpSyncClient client) {

        return builder
                .defaultSystem(AdoptionAssistantController.GLOBAL_PROMPT)
                .defaultToolCallbacks(new SyncMcpToolCallbackProvider(client))
                .defaultAdvisors(advisor)
                .build();
    }

}
