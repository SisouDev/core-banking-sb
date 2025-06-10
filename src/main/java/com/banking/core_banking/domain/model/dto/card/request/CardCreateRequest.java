package com.banking.core_banking.domain.model.dto.card.request;

import jakarta.validation.constraints.NotNull;

public record CardCreateRequest(
        @NotNull Long accountId
) {
}
