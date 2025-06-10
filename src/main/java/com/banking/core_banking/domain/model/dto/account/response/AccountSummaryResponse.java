package com.banking.core_banking.domain.model.dto.account.response;

import java.math.BigDecimal;

public record AccountSummaryResponse(
        Long id,
        String agency,
        String number,
        BigDecimal balance,
        String accountType
) {
}
