package com.banking.core_banking.domain.model.dto.card.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record DebitFunctionActivateRequest(
        @NotNull @PositiveOrZero BigDecimal dailyWithdrawalLimit,
        @NotNull @PositiveOrZero BigDecimal dailyTransactionLimit
) {
}
