package com.jaydee.School.Exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.EqualsAndHashCode;

@Getter
@EqualsAndHashCode(callSuper=false)
public class ApiException extends RuntimeException {
	private static final long serialVersionUID = 1L;
    private HttpStatus status;
    private String message;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public ApiException(String message) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.message = message;
    }
}
