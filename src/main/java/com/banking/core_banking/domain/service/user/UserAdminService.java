package com.banking.core_banking.domain.service.user;

import com.banking.core_banking.domain.model.dto.admin.request.AdminUserRoleUpdateRequest;
import com.banking.core_banking.domain.model.dto.admin.request.AdminUserStatusUpdateRequest;
import com.banking.core_banking.domain.model.dto.user.response.UserResponse;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

public interface UserAdminService {
    Page<UserResponse> listAllUsers(Pageable pageable);
    UserResponse getUserById(Long userId);
    UserResponse changeUserStatus(Long userId, AdminUserStatusUpdateRequest request);
    UserResponse changeUserRole(Long userId, AdminUserRoleUpdateRequest request);
}
