package com.example.backendDemo.github.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GithubRepoDto(
        List<Item> items
) {

    public record Item(
            int id,
            String name,
            String language,
            int stargazers_count,
            LocalDateTime created_at
    ) {
    }
}