package com.career.guide.backend.util;

import com.career.guide.backend.dto.common.ApiResponse;
import com.career.guide.backend.dto.common.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseHelper {
    
    // Success responses
    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok(ApiResponse.success(data));
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
        return ResponseEntity.ok(ApiResponse.success(message, data));
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> success(String message) {
        return ResponseEntity.ok(ApiResponse.success(message));
    }
    
    // Created response
    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        ApiResponse<T> response = ApiResponse.success("Resource created successfully", data);
        response.setStatusCode(201);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
        ApiResponse<T> response = ApiResponse.success(message, data);
        response.setStatusCode(201);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    // Updated response
    public static <T> ResponseEntity<ApiResponse<T>> updated(T data) {
        return ResponseEntity.ok(ApiResponse.success("Resource updated successfully", data));
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> updated(String message, T data) {
        return ResponseEntity.ok(ApiResponse.success(message, data));
    }
    
    // Deleted response
    public static <T> ResponseEntity<ApiResponse<T>> deleted() {
        return ResponseEntity.ok(ApiResponse.success("Resource deleted successfully"));
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> deleted(String message) {
        return ResponseEntity.ok(ApiResponse.success(message));
    }
    
    // Paged response
    public static <T> ResponseEntity<ApiResponse<PagedResponse<T>>> paged(Page<T> page) {
        PagedResponse<T> pagedResponse = PagedResponse.of(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements()
        );
        return ResponseEntity.ok(ApiResponse.success(pagedResponse));
    }
    
    public static <T> ResponseEntity<ApiResponse<PagedResponse<T>>> paged(List<T> content, int page, int size, long totalElements) {
        PagedResponse<T> pagedResponse = PagedResponse.of(content, page, size, totalElements);
        return ResponseEntity.ok(ApiResponse.success(pagedResponse));
    }
    
    // Error responses
    public static <T> ResponseEntity<ApiResponse<T>> badRequest(String message) {
        return ResponseEntity.badRequest().body(ApiResponse.validationError(message));
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> notFound(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.notFound(message));
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> unauthorized(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.unauthorized(message));
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> forbidden(String message) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.forbidden(message));
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> internalServerError(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(message, HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
