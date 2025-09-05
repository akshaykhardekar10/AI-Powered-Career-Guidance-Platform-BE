package com.career.guide.backend.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    private boolean success;
    private String message;
    private T data;
    private String error;
    private String path;
    private int statusCode;
    private LocalDateTime timestamp;
    
    // Success response with data
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message("Operation completed successfully")
                .data(data)
                .statusCode(200)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    // Success response with custom message and data
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .statusCode(200)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    // Success response with only message (no data)
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .statusCode(200)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    // Error response
    public static <T> ApiResponse<T> error(String message, int statusCode) {
        return ApiResponse.<T>builder()
                .success(false)
                .message("Operation failed")
                .error(message)
                .statusCode(statusCode)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    // Error response with path
    public static <T> ApiResponse<T> error(String message, int statusCode, String path) {
        return ApiResponse.<T>builder()
                .success(false)
                .message("Operation failed")
                .error(message)
                .statusCode(statusCode)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    // Validation error response
    public static <T> ApiResponse<T> validationError(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message("Validation failed")
                .error(message)
                .statusCode(400)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    // Not found error response
    public static <T> ApiResponse<T> notFound(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message("Resource not found")
                .error(message)
                .statusCode(404)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    // Unauthorized error response
    public static <T> ApiResponse<T> unauthorized(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message("Unauthorized access")
                .error(message)
                .statusCode(401)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    // Forbidden error response
    public static <T> ApiResponse<T> forbidden(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message("Access forbidden")
                .error(message)
                .statusCode(403)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
