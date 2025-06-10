package com.banking.core_banking.domain.model.dto.card.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreditFunctionActivateRequest(
        @NotNull @Positive BigDecimal creditLimit,
        @NotNull @Min(1) @Max(28) Integer invoiceClosingDay
) {
}
