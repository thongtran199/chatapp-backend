package com.springboot.chatapp.model.exception;

import org.springframework.http.HttpStatus;

public class ChatAppAPIException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public ChatAppAPIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public ChatAppAPIException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
