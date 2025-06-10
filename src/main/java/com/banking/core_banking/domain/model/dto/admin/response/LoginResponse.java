package com.banking.core_banking.domain.model.dto.admin.response;

import com.banking.core_banking.domain.model.dto.user.response.UserResponse;

public record LoginResponse(
        String accessToken,
        Long expiresIn,
        UserResponse user
) {
}
