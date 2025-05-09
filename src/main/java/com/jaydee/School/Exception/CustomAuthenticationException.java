package com.jaydee.School.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class CustomAuthenticationException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public CustomAuthenticationException(String msg) {
        super(msg);
    }

    public CustomAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}