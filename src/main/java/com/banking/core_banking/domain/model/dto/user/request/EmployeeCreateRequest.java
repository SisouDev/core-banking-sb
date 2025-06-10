package com.banking.core_banking.domain.model.dto.user.request;

import com.banking.core_banking.domain.model.enums.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EmployeeCreateRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,

        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password,

        @NotNull Role role
) {
}
