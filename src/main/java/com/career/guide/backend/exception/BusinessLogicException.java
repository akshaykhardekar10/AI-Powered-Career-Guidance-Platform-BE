package com.career.guide.backend.exception;

import org.springframework.http.HttpStatus;

public class BusinessLogicException extends CustomException {
    
    public BusinessLogicException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY, "BUSINESS_LOGIC_ERROR");
    }
    
    public BusinessLogicException(String message, Throwable cause) {
        super(message, cause, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
