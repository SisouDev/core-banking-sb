package com.banking.core_banking.domain.model.dto.admin.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email cannot be blank.")
        @Email(message = "Please provide a valid email address.")
        String email,

        @NotBlank(message = "Password cannot be blank.")
        String password
) {
}
