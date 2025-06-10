package com.banking.core_banking.domain.model.dto.user.response;

import java.time.LocalDate;

public record PersonalCustomerResponse(
        Long id,
        String phone,
        String name,
        String registrationNumber,
        LocalDate birthDate
) implements CustomerResponse {
}
