package com.career.guide.backend.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends CustomException {
    
    public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "VALIDATION_ERROR");
    }
    
    public ValidationException(String field, String message) {
        super(String.format("Validation failed for field '%s': %s", field, message), 
              HttpStatus.BAD_REQUEST, "VALIDATION_ERROR");
    }
}
