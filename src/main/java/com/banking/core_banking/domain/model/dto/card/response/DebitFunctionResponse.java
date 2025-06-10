package com.banking.core_banking.domain.model.dto.card.response;

import java.math.BigDecimal;

public record DebitFunctionResponse(
        BigDecimal dailyWithdrawalLimit,
        BigDecimal dailyTransactionLimit
) {
}
