package com.banking.core_banking.domain.model.dto.account.response;

import com.banking.core_banking.domain.model.dto.user.response.CustomerSummaryResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AccountDetailsResponse(
        Long id,
        String agency,
        String number,
        BigDecimal balance,
        String accountType,
        String accountStatus,
        LocalDateTime createdAt,
        CustomerSummaryResponse customer,
        String accountManagerName,

        List<TransactionResponse> recentTransactions
) {
}
