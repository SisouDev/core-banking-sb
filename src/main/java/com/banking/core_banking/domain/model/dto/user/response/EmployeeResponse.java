package com.banking.core_banking.domain.model.dto.user.response;

import java.time.LocalDateTime;

public record EmployeeResponse(
        Long id,
        String firstName,
        String lastName,
        String registrationCode,
        LocalDateTime hireDate,

        Long userId,
        String email,
        String role
) {
}
