package com.banking.core_banking.domain.model.dto.invoice.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvoiceItemResponse(
        String description,
        BigDecimal amount,
        LocalDateTime purchaseDate
) {
}
