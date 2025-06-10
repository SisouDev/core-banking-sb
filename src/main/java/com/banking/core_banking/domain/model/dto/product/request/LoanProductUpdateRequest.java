package com.banking.core_banking.domain.model.dto.product.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record LoanProductUpdateRequest(
        String name,
        String description,
        Boolean active,

        @PositiveOrZero BigDecimal minAmount,
        @Positive BigDecimal maxAmount,
        @Positive BigDecimal defaultInterestRate,
        @Positive Integer maxInstallments
) {
}
