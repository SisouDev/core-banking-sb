package com.banking.core_banking.domain.model.dto.card.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreditLimitUpdateRequest(
        @NotNull @Positive BigDecimal newCreditLimit
) {
}
