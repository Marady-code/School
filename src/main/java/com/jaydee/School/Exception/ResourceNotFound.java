package com.jaydee.School.Exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFound extends ApiException {
	private static final long serialVersionUID = 1L;
    public ResourceNotFound(String resourceName, Long id){
        super(HttpStatus.NOT_FOUND, String.format("%s with id = %d not found!",resourceName, id ));
    }
	
	
}
