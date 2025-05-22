package com.animal.adoption.config;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

import javax.sql.DataSource;

@Configuration
public class Beans {

    @Bean
    public JdbcClient jdbcClient(DataSource dataSource){
        return JdbcClient.create(dataSource);
    }

	/*@Bean
	ApplicationRunner vectorStoreInitializer(DogRepo dogRepo,
											 VectorStore vectorStore) {
		return args -> {
			List<Dog> dogs = dogRepo.findAll();
			System.out.println("Found " + dogs.size() + " dogs from DogRepo.");
			List<Document> dogDocs = new ArrayList<>();
			dogs.forEach(dog -> {
						Document d = new Document("id: %s, name: %s, description: %s"
						.formatted(dog.id(), dog.name(), dog.description()));
						dogDocs.add(d);
			});
			vectorStore.add(dogDocs);
		};

	}*/

    @Bean
    public QuestionAnswerAdvisor questionAnswerAdvisor(VectorStore vectorStore){
        return new QuestionAnswerAdvisor(vectorStore);
    }

    @Bean
    public McpSyncClient mcpSyncClient(@Value("${ADOPTIONS_SCHEDULER_HOST:localhost:8081}") String host){
        var mcp = McpClient
                .sync(HttpClientSseClientTransport.builder("http://" + host)
                        .build())
                .build();
        mcp.initialize();
        return mcp ;
    }
}
