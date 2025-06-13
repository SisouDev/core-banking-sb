package com.banking.core_banking.domain.model.dto.card.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreditPurchaseRequest(
        @NotNull Long cardId,
        @NotNull @Positive BigDecimal amount,
        @NotBlank String merchantName
) {
}
