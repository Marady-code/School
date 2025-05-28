package com.jaydee.School.Exception;

public class ResourceNotFound extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResourceNotFound(String message) {
		super(message);
	}

	public ResourceNotFound(String resourceName, String id) {
		super(String.format("%s not found with id: %s", resourceName, id));
	}

	public ResourceNotFound(String resourceName, Long id) {
		super(String.format("%s not found with id: %d", resourceName, id));
	}
}
