package com.furia.challenge.api.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WebClientService {

    @Value("${PANDASCORE_TOKEN}")
    private String pandasCoreToken;
    
    public String webClient(String uri){
        WebClient client = WebClient.create("https://api.pandascore.co");
        
        String response = client.get()
                .uri(uri)
                .header("Authorization", String.format("Bearer %s", pandasCoreToken))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;
    }
}
