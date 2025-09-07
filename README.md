
#AGENTIC AI 

An Agentic AI application built on the Spring Boot framework that automates animal adoption and care processes. 
This prototype validates the core architecture for a conversational assistant that can understand user intent, retrieve contextual information, and perform real-world actions

🌟 Features

    Intelligent Routing: A router-agent architecture that dynamically delegates user queries to specialized AI agents based on intent.

    Retrieval Augmented Generation (RAG): Integrates with a PostgreSQL Vector database to provide accurate, data-driven responses using contextual information.

    Automated Tool Calling: Enables the AI to perform business logic (e.g., booking appointments) by calling a custom tool.

    Conversational Memory: Maintains per-user chat history to support coherent, multi-turn interactions.

    Modular & Scalable: Designed with a clean, loosely coupled architecture to easily integrate new features and tools.


🚀 Technologies Used

    Backend Framework: Spring Boot 3.x

    AI Framework: Spring AI

    Database: PostgreSQL with the PGVector extension

    LLM Provider: OpenAI

    Build Tool: Maven

Getting Started

This guide will help you get a local copy of the project up and running for development and testing.

1. Prerequisites

    Java Development Kit (JDK) 17 or higher

    Maven 3.6.3 or higher

    Docker (for running the PostgreSQL database with PGVector)
   
2. Setup
        git clone https://github.com/wizzy-abhishek/MCP

    Configure Environment Variables:
   
        Create an application.properties file in src/main/resources and add your configurations.
    
        # Your Database connection
        spring.datasource.url=jdbc:postgresql://localhost:5432/your_db_name
        spring.datasource.username=your_username
        spring.datasource.password=your_password
        
        # Your LLM API key
        spring.ai.openai.api-key=your_openai_api_key
    
        # RAG Vector Store configuration
        spring.ai.vectorstore.pgvector.embedding-dimension=1536

    Run the Database:
        You can use Docker to run a PostgreSQL instance with the PGVector extension.
       docker run --name pgvector -e POSTGRES_PASSWORD=your_password -p 5432:5432 -d ankane/pgvector
       Then, create the your_db_name database.

3. Running the Application
    You can run the application directly from your IDE or using Maven:
       ./mvnw spring-boot:run
   
The application will start on port 8080.
