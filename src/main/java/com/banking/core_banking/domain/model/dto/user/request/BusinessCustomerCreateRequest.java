package com.banking.core_banking.domain.model.dto.user.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BusinessCustomerCreateRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password,

        @NotBlank String companyName,
        String tradeName,
        @NotBlank String registrationNumber,

        String phone,
        @NotNull @Valid AddressCreateRequest address
) {
}
