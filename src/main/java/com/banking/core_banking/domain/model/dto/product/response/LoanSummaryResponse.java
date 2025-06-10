package com.banking.core_banking.domain.model.dto.product.response;

import java.math.BigDecimal;

public record LoanSummaryResponse(
        Long id,
        String productName,
        BigDecimal principalAmount,
        String status,
        Integer numberOfInstallments
) {
}
