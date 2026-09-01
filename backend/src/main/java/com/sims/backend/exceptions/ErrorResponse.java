package com.sims.backend.dtos;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponse {

    private String message;
    private int status;
    private LocalDateTime timestamp;
    private Map<String, String> errors;

    public ErrorResponse(String message, int status, LocalDateTime timestamp) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }

    public ErrorResponse(String message, int status, LocalDateTime timestamp, Map<String, String> errors) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
        this.errors = errors;
    }

    public static ErrorResponse of(String message, int status) {
        return new ErrorResponse(message, status, LocalDateTime.now());
    }

    public static ErrorResponse of(String message, int status, Map<String, String> errors) {
        return new ErrorResponse(message, status, LocalDateTime.now(), errors);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
