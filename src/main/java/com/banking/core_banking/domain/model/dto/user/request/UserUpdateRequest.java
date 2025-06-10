package com.banking.core_banking.domain.model.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Email(message = "If provided, email must be a valid email address.")
        String email
) {
}
