package com.banking.core_banking.domain.model.dto.account.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DepositRequest(
        @NotNull
        @Positive(message = "Deposit amount must be positive.")
        BigDecimal amount
) {
}
