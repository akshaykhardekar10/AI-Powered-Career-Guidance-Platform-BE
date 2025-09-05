package com.career.guide.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @Email
    @NotBlank
    private String email;
    
    @NotBlank
    private String token;
    
    @NotBlank
    @Size(min = 8)
    private String newPassword;
}
