package com.banking.core_banking.domain.model.dto.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record LoanProductCreateRequest(
        @NotBlank String name,
        @NotBlank String description,
        @NotNull @PositiveOrZero BigDecimal minAmount,
        @NotNull @Positive BigDecimal maxAmount,
        @NotNull @Positive BigDecimal defaultInterestRate,
        @NotNull @Positive Integer maxInstallments
) {
}
