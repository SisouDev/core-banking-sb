package com.banking.core_banking.domain.model.dto.account.request;

import com.banking.core_banking.domain.model.enums.account.AccountType;
import jakarta.validation.constraints.NotNull;

public record AccountCreateRequest(
        @NotNull Long customerId,
        @NotNull AccountType accountType,
        Long managerId
) {
}
