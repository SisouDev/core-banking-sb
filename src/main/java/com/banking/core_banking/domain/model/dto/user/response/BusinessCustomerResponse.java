package com.banking.core_banking.domain.model.dto.user.response;

public record BusinessCustomerResponse(
        Long id,
        String phone,
        String companyName,
        String tradeName,
        String registrationNumber
) implements CustomerResponse {
}
