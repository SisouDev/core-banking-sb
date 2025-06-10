package com.banking.core_banking.domain.model.dto.card.response;

import com.banking.core_banking.domain.model.entities.invoice.Invoice;

import java.math.BigDecimal;
import java.util.List;

public record CreditFunctionResponse(
        BigDecimal creditLimit,
        BigDecimal availableLimit,
        Integer invoiceClosingDay
) {
}
