package com.banking.core_banking.domain.model.dto.product.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanInstallmentResponse(
        Integer installmentNumber,
        BigDecimal totalAmount,
        BigDecimal principalAmount,
        BigDecimal interestAmount,
        LocalDate dueDate,
        String status
) {
}
