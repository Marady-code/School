package com.jaydee.School.Exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private Long id;
    private HttpStatus status;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(Long id, HttpStatus status, String message) {
        this.id = id;
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
