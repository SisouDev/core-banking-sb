package com.banking.core_banking.domain.service.others;

import com.banking.core_banking.domain.model.dto.admin.request.LoginRequest;
import com.banking.core_banking.domain.model.dto.admin.response.LoginResponse;
import com.banking.core_banking.domain.model.dto.user.request.PasswordChangeRequest;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    void changePassword(Long userId, PasswordChangeRequest request);
}
