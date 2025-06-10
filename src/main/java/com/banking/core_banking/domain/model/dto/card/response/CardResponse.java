package com.banking.core_banking.domain.model.dto.card.response;

import java.time.YearMonth;

public record CardResponse(
        Long id,
        String maskedNumber,
        String holderName,
        YearMonth expirationDate,
        String status,

        DebitFunctionResponse debitFunction,
        CreditFunctionResponse creditFunction
) {
}
