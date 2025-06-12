package com.banking.core_banking.domain.model.dto.product.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record LoanApplicationRequest(
        @NotNull Long productId,
        @NotNull @Positive BigDecimal principalAmount,
        @NotNull @Positive Integer numberOfInstallments
) {
}
