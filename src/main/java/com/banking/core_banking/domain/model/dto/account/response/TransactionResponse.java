package com.banking.core_banking.domain.model.dto.account.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        BigDecimal amount,
        String transactionType,
        String description,
        LocalDateTime timestamp
) {
}
