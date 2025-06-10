package com.banking.core_banking.domain.model.dto.product.response;

import com.banking.core_banking.domain.model.dto.user.response.CustomerSummaryResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record LoanDetailsResponse(
        Long id,
        String productName,
        BigDecimal principalAmount,
        BigDecimal interestRate,
        String status,
        LocalDateTime disbursementDate,
        Integer numberOfInstallments,
        CustomerSummaryResponse customer,

        List<LoanInstallmentResponse> installments
) {
}
