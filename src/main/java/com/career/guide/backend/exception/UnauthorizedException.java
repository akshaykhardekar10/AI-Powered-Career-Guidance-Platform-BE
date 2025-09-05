package com.career.guide.backend.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends CustomException {
    
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED_ACCESS");
    }
    
    public UnauthorizedException() {
        super("Access denied. Please authenticate to continue.", 
              HttpStatus.UNAUTHORIZED, "UNAUTHORIZED_ACCESS");
    }
}
