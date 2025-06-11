package com.banking.core_banking.infra.service.user;

import com.banking.core_banking.domain.model.dto.admin.request.AdminUserRoleUpdateRequest;
import com.banking.core_banking.domain.model.dto.admin.request.AdminUserStatusUpdateRequest;
import com.banking.core_banking.domain.model.dto.user.response.UserResponse;
import com.banking.core_banking.domain.model.entities.user.User;
import com.banking.core_banking.domain.repository.user.UserRepository;
import com.banking.core_banking.domain.service.user.UserAdminService;
import com.banking.core_banking.exceptions.others.ResourceNotFoundException;
import com.banking.core_banking.infra.mapper.user.UserMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserAdminServiceImpl implements UserAdminService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Autowired
    public UserAdminServiceImpl(
            UserMapper userMapper,
            UserRepository userRepository
    ) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    public Page<UserResponse> listAllUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(userMapper::toDto);
    }

    @Override
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserResponse changeUserStatus(Long userId, AdminUserStatusUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        user.setUserStatus(request.newStatus());
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public UserResponse changeUserRole(Long userId, AdminUserRoleUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        user.setRole(request.newRole());
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }
}
