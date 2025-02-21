package com.jaydee.School.Exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {
	
//	@ExceptionHandler(ApiException.class)
//	public ResponseEntity<?> handleApiException(ApiException e){
//		ErrorResponse errorResponse = new ErrorResponse(e.getStatus(), e.getMessage());
//		return ResponseEntity.status(e.getStatus()).body(errorResponse);
//	}
	
	@ExceptionHandler(ResourceNotFound.class)
	public ResponseEntity<?> handleResourceNotFound(ResourceNotFound ex){
		Map<String, Object> response = new HashMap<>();
		response.put("timestamp", LocalDateTime.now());
		response.put("status", ex.getStatus().value());
		response.put("error", "Not Found");
		response.put("message", ex.getMessage());
		
		return ResponseEntity.status(ex.getStatus()).body(response);
	}
	
}
