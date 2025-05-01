package com.furia.challenge.api.configuration.modelConfig;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelConfiguration {
    
    @Bean
    public ModelMapper getModelMapper(){
        return new ModelMapper();
    }
}
