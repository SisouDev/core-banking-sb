package com.banking.core_banking.domain.service.others;

import com.banking.core_banking.domain.model.dto.admin.request.LoginRequest;
import com.banking.core_banking.domain.model.dto.admin.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
