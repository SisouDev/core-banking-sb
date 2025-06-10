package com.banking.core_banking.domain.model.dto.user.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record PersonalCustomerCreateRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password,

        @NotBlank String name,
        @NotBlank String registrationNumber, // CPF
        @NotNull @Past LocalDate birthDate,

        String phone,
        @NotNull @Valid AddressCreateRequest address
) {
}
