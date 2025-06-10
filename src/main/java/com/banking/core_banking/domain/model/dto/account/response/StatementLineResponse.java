package com.banking.core_banking.domain.model.dto.account.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StatementLineResponse(
        Long transactionId,
        BigDecimal amount,
        String transactionType,
        String description,
        LocalDateTime timestamp,

        BigDecimal runningBalance
) {
}
