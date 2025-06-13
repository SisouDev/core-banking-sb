package com.banking.core_banking.domain.model.dto.account.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record WithdrawRequest(
        @NotNull
        @Positive(message = "Withdrawal amount must be positive.")
        BigDecimal amount,
        String message
) {
}
