package com.banking.core_banking.domain.model.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordChangeRequest(
        @NotBlank(message = "Old password cannot be blank.")
        String oldPassword,

        @NotBlank(message = "New password cannot be blank.")
        @Size(min = 8, message = "New password must have at least 8 characters.")
        String newPassword,

        @NotBlank(message = "Confirmation password cannot be blank.")
        String confirmNewPassword
) {
}
