package com.jaydee.School.Exception;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationErrorDetails extends ErrorDetails {
    
    private final Map<String, String> errors;
    
    public ValidationErrorDetails(LocalDateTime timestamp, String message, String details, int statusCode, Map<String, String> errors) {
        super(timestamp, message, details, statusCode);
        this.errors = errors;
    }
}