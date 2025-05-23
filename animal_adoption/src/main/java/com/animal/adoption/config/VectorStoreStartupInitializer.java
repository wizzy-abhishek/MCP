package com.animal.adoption.config;


import com.animal.adoption.client.Dog;
import com.animal.adoption.repo.DogRepo;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
class VectorStoreStartupInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final JdbcTemplate jdbcTemplate;
    private final DogRepo dogRepo;
    private final VectorStore vectorStore;
    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    public VectorStoreStartupInitializer(JdbcTemplate jdbcTemplate, DogRepo dogRepo, VectorStore vectorStore) {
        this.jdbcTemplate = jdbcTemplate;
        this.dogRepo = dogRepo;
        this.vectorStore = vectorStore;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {

            jdbcTemplate.execute("TRUNCATE TABLE vector_store");
            System.out.println("Vector store table truncated.");

            List<Dog> dogs = dogRepo.findAll();
            System.out.println("Found " + dogs.size() + " dogs from DogRepo.");

            for (Dog dog : dogs) {
                String content = "id: %s, name: %s, description: %s , owner: %s"
                        .formatted(dog.id(), dog.name(), dog.description(), dog.owner());
                Document doc = new Document(content);

                CompletableFuture.runAsync(() -> {
                    try {
                        vectorStore.add(List.of(doc));
                        System.out.println("Successfully added: " + content);
                    } catch (Exception e) {
                        System.err.println("Failed to add: " + content);
                        e.printStackTrace();
                    }
                }, executor);
            }

            System.out.println("Dog vector insertion tasks submitted.");

        } catch (Exception e) {
            System.err.println("Error during vector store initialization:");
            e.printStackTrace();
        }
    }
}
