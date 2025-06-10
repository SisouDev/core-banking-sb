package com.banking.core_banking.domain.model.dto.admin.request;

import com.banking.core_banking.domain.model.enums.user.Role;
import jakarta.validation.constraints.NotNull;

public record AdminUserRoleUpdateRequest(
        @NotNull(message = "New role cannot be null.")
        Role newRole
) {
}
