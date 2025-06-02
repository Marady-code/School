package com.jaydee.School.Exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private String details;
    private LocalDateTime timestamp;

    public ErrorResponse(HttpStatus status, String message, String details) {
        this.status = status.value();
        this.message = message;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }
}
