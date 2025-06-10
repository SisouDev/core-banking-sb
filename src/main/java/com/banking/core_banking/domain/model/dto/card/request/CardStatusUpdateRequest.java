package com.banking.core_banking.domain.model.dto.card.request;

import com.banking.core_banking.domain.model.enums.card.CardStatus;
import jakarta.validation.constraints.NotNull;

public record CardStatusUpdateRequest(
        @NotNull CardStatus newStatus
) {
}
