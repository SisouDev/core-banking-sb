package com.banking.core_banking.domain.controller.user;

import com.banking.core_banking.domain.model.dto.admin.request.AdminUserRoleUpdateRequest;
import com.banking.core_banking.domain.model.dto.admin.request.AdminUserStatusUpdateRequest;
import com.banking.core_banking.domain.model.dto.user.response.UserResponse;
import com.banking.core_banking.domain.service.user.UserAdminService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserAdminController {

    private final UserAdminService userAdminService;

    public UserAdminController(UserAdminService userAdminService) {
        this.userAdminService = userAdminService;
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> listAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userAdminService.listAllUsers(pageable));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userAdminService.getUserById(userId));
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<UserResponse> changeUserStatus(
            @PathVariable Long userId,
            @Valid @RequestBody AdminUserStatusUpdateRequest request
    ) {
        return ResponseEntity.ok(userAdminService.changeUserStatus(userId, request));
    }

    @PatchMapping("/{userId}/role")
    public ResponseEntity<UserResponse> changeUserRole(
            @PathVariable Long userId,
            @Valid @RequestBody AdminUserRoleUpdateRequest request
    ) {
        return ResponseEntity.ok(userAdminService.changeUserRole(userId, request));
    }
}