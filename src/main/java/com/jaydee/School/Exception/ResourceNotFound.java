package com.jaydee.School.Exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFound extends ApiException {
    public ResourceNotFound(Long id){
        super(HttpStatus.NOT_FOUND, String.format("Student with id = %d not found", id ));
    }
}
