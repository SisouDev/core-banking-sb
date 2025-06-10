package com.banking.core_banking.domain.model.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressCreateRequest(
        @NotBlank String street,
        @NotBlank @Size(max = 10) String number,
        String complement,
        @NotBlank String neighborhood,
        @NotBlank String city,
        @NotBlank @Size(min = 2, max = 2) String state,
        @NotBlank @Size(min = 8, max = 9) String zipCode,
        @NotBlank @Size(min = 2, max = 2) String countryCode
)
{
}
