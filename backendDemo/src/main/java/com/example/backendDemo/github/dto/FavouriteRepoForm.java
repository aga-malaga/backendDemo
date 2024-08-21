package com.example.backendDemo.github.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record FavouriteRepoForm(
        @NotNull
        Integer id,
        @NotNull @Size(max = 1000)
        String name,
        @NotNull @Size(max = 100)
        String language,
        @NotNull
        Integer stargazers_count,
        @NotNull @DateTimeFormat
        LocalDateTime created_at
) {
}