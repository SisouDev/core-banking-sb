package com.banking.core_banking.domain.model.dto.invoice.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public record InvoiceSummaryResponse(
        Long id,
        YearMonth referenceMonth,
        BigDecimal totalAmount,
        LocalDate dueDate,
        String status
) {
}
