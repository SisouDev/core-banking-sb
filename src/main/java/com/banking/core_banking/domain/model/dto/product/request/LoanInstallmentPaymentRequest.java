package com.banking.core_banking.domain.model.dto.product.request;

import jakarta.validation.constraints.NotNull;

public record LoanInstallmentPaymentRequest(
        @NotNull Long sourceAccountId
) {
}
