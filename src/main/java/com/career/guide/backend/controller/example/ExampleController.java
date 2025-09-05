package com.career.guide.backend.controller.example;

import com.career.guide.backend.dto.common.ApiResponse;
import com.career.guide.backend.exception.ResourceNotFoundException;
import com.career.guide.backend.exception.ValidationException;
import com.career.guide.backend.util.ResponseHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Example controller demonstrating how to use the new API Response Handler and Error Handler
 * This controller shows various response patterns and error scenarios
 */
@RestController
@RequestMapping("/api/example")
public class ExampleController {

    // Example of successful response with data
    @GetMapping("/success")
    public ResponseEntity<ApiResponse<Map<String, String>>> successExample() {
        Map<String, String> data = new HashMap<>();
        data.put("message", "This is a successful response");
        data.put("status", "active");
        
        return ResponseHelper.success("Operation completed successfully", data);
    }

    // Example of created response
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createExample(@RequestBody Map<String, String> request) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", 123L);
        data.put("name", request.get("name"));
        data.put("created", true);
        
        return ResponseHelper.created("Resource created successfully", data);
    }

    // Example of updated response
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateExample(@PathVariable Long id, @RequestBody Map<String, String> request) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("name", request.get("name"));
        data.put("updated", true);
        
        return ResponseHelper.updated("Resource updated successfully", data);
    }

    // Example of deleted response
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteExample(@PathVariable Long id) {
        return ResponseHelper.deleted("Resource with id " + id + " deleted successfully");
    }

    // Example of ResourceNotFoundException
    @GetMapping("/not-found/{id}")
    public ResponseEntity<ApiResponse<Object>> notFoundExample(@PathVariable Long id) {
        throw new ResourceNotFoundException("User", "id", id);
    }

    // Example of ValidationException
    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<Object>> validationExample(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || !email.contains("@")) {
            throw new ValidationException("email", "Email must be valid and contain @ symbol");
        }
        
        return ResponseHelper.success("Validation passed");
    }

    // Example of IllegalArgumentException (handled by GlobalExceptionHandler)
    @GetMapping("/illegal-argument")
    public ResponseEntity<ApiResponse<Object>> illegalArgumentExample(@RequestParam String value) {
        if ("invalid".equals(value)) {
            throw new IllegalArgumentException("Invalid value provided: " + value);
        }
        
        return ResponseHelper.success("Valid value received");
    }

    // Example of generic Exception (handled by GlobalExceptionHandler)
    @GetMapping("/generic-error")
    public ResponseEntity<ApiResponse<Object>> genericErrorExample() {
        throw new RuntimeException("This is a generic runtime exception for testing");
    }
}
