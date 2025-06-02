package com.jaydee.School.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    private final String message;
    private final String details;

    public BadRequestException(String message) {
        super(message);
        this.message = message;
        this.details = null;
    }

    public BadRequestException(String message, String details) {
        super(message);
        this.message = message;
        this.details = details;
    }
}