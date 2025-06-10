package com.banking.core_banking.domain.model.dto.admin.request;

import com.banking.core_banking.domain.model.enums.user.UserStatus;
import jakarta.validation.constraints.NotNull;

public record AdminUserStatusUpdateRequest(
        @NotNull UserStatus newStatus
) {
}
