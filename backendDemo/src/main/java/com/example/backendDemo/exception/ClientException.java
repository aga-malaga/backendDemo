package com.example.backendDemo.exception;

public class ClientException extends RuntimeException {

    private static final String MESSAGE_FORMAT = "Client exception with status %s. Response body: %s";

    public ClientException(int statusCode, String message) {
        super(MESSAGE_FORMAT.formatted(statusCode, message));
    }
}