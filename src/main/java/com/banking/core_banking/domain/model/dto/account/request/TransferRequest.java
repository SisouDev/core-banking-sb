package com.banking.core_banking.domain.model.dto.account.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequest(
        @NotBlank String destinationAccountNumber,

        @NotNull @Positive BigDecimal amount,

        String description
) {
}
