package com.example.backendDemo.github.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record SearchRepoForm(
        @Min(10)
        @Max(100)
        @Digits(integer = 3, fraction = 0)
        Integer perPage,
        @NotNull
        @DateTimeFormat
        LocalDate createdAt,
        @Size(min = 1, max = 100)
        String language
) {
}