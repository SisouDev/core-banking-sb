package com.banking.core_banking.domain.model.dto.user.response;

import com.banking.core_banking.domain.model.enums.user.Role;

public record UserResponse(
        Long id,
        String email,
        String userStatus,
        Role role
) {
}
