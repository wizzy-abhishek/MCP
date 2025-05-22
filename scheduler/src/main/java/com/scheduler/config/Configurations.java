package com.scheduler.config;

import com.scheduler.service.AnimalAdoptionScheduler;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configurations {

    @Bean
    public MethodToolCallbackProvider methodToolCallbackProvider(AnimalAdoptionScheduler scheduler){
        return MethodToolCallbackProvider
                .builder()
                .toolObjects(scheduler)
                .build();
    }
}
