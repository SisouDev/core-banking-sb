package com.banking.core_banking.domain.model.dto.card.response;

public record CardSummaryResponse(
        Long id,
        String maskedNumber,
        String holderName,
        String status,
        boolean isDebit,
        boolean isCredit
) {
}
