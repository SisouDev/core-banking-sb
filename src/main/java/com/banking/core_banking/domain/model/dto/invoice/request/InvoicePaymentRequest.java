package com.banking.core_banking.domain.model.dto.invoice.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record InvoicePaymentRequest(
        @NotNull @Positive BigDecimal paymentAmount
) {
}
