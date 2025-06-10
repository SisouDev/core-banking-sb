package com.banking.core_banking.domain.model.dto.invoice.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public record InvoiceDetailsResponse(
        Long id,
        YearMonth referenceMonth,
        BigDecimal totalAmount,
        BigDecimal paidAmount,
        BigDecimal dueAmount,
        LocalDate dueDate,
        LocalDate closingDate,
        String status,
        List<InvoiceItemResponse> items
) {
}
