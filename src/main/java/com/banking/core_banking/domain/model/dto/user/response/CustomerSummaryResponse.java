package com.banking.core_banking.domain.model.dto.user.response;

public record CustomerSummaryResponse(
        Long id,
        String displayName,
        String customerType,
        String registrationNumber
) {
}
