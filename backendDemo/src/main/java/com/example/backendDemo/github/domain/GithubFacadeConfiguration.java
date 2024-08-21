package com.example.backendDemo.github.domain;


import com.example.backendDemo.github.GithubFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
class GithubFacadeConfiguration {

    @Bean
    GithubFacade githubFacade(RestClient restClient) {
        return new GithubServiceImpl(restClient);
    }

    @Bean
    RestClient restClient(RestClient.Builder builder) {
        return builder
                .build();
    }
}