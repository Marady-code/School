package com.jaydee.School.Exception;

// import java.time.LocalDateTime; // Not used
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestException(InvalidRequestException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            ex.getMessage(),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFound.class)
    protected ResponseEntity<Object> handleResourceNotFound(
            ResourceNotFound ex, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequest(
            BadRequestException ex, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        if (ex.getDetails() != null) {
            apiError.setDebugMessage(ex.getDetails());
        }
        return buildResponseEntity(apiError);
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage("Validation error");
        List<ApiSubError> subErrors = new ArrayList<>();
        
        ex.getBindingResult().getAllErrors().forEach(error -> {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                subErrors.add(new ApiValidationError(
                    fieldError.getObjectName(),
                    fieldError.getField(),
                    fieldError.getRejectedValue(),
                    fieldError.getDefaultMessage()));
            } else {
                subErrors.add(new ApiValidationError(
                    error.getObjectName(),
                    error.getDefaultMessage()));
            }
        });
        
        apiError.setSubErrors(subErrors);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage("Validation error");
        List<ApiSubError> subErrors = new ArrayList<>();
        
        ex.getConstraintViolations().forEach(violation -> {
            subErrors.add(new ApiValidationError(
                violation.getRootBeanClass().getName(),
                violation.getPropertyPath().toString(),
                violation.getInvalidValue(),
                violation.getMessage()));
        });
        
        apiError.setSubErrors(subErrors);
        return buildResponseEntity(apiError);
    }    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDenied(
            org.springframework.security.access.AccessDeniedException ex, WebRequest request) {
        ApiError apiError = new ApiError(
            HttpStatus.FORBIDDEN,
            "Access denied: You don't have permission to access this resource",
            ex);
        return buildResponseEntity(apiError);
    }
    
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllOtherExceptions(
            Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred",
            ex);
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
