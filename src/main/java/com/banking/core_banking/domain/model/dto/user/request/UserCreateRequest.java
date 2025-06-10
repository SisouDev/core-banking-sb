package com.banking.core_banking.domain.model.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank(message = "Email cannot be blank.")
        @Email(message = "Email must be a valid email address.")
        String email,

        @NotBlank(message = "Password cannot be blank.")
        @Size(min = 8, message = "Password must have at least 8 characters.")
        String password
) {
}
