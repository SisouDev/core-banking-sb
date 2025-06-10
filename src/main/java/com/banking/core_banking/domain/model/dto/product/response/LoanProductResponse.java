package com.banking.core_banking.domain.model.dto.product.response;

import java.math.BigDecimal;

public record LoanProductResponse(
        Long id,
        String name,
        String description,
        boolean isActive,
        BigDecimal minAmount,
        BigDecimal maxAmount,
        BigDecimal defaultInterestRate,
        Integer maxInstallments
) implements BankingProductResponse
{
}
