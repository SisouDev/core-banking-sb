package com.banking.core_banking.unit.service.user;

import com.banking.core_banking.domain.model.dto.admin.request.AdminUserStatusUpdateRequest;
import com.banking.core_banking.domain.model.dto.user.response.UserResponse;
import com.banking.core_banking.domain.model.entities.user.User;
import com.banking.core_banking.domain.model.enums.user.Role;
import com.banking.core_banking.domain.model.enums.user.UserStatus;
import com.banking.core_banking.domain.repository.user.UserRepository;
import com.banking.core_banking.exceptions.others.ResourceNotFoundException;
import com.banking.core_banking.infra.mapper.user.UserMapper;
import com.banking.core_banking.infra.service.user.UserAdminServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserAdminServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserAdminServiceImpl userAdminService;

    @Test
    void whenUserExists_shouldChangeStatusAndReturnUpdatedDto() {
        Long userId = (Long) 1L;
        AdminUserStatusUpdateRequest request = new AdminUserStatusUpdateRequest(UserStatus.BLOCKED);
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUserStatus(UserStatus.ACTIVE);

        UserResponse expectedResponse  = new UserResponse(userId, "test@test.com", "BLOCKED", Role.CUSTOMER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(userMapper.toDto(any(User.class))).thenReturn(expectedResponse);

        UserResponse actualResponse = userAdminService.changeUserStatus(userId, request);

        assertNotNull(actualResponse);
        assertEquals(UserStatus.BLOCKED.name(), actualResponse.userStatus());

        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void whenUserNotFound_shouldThrowResourceNotFoundException() {
        Long nonExistentUserId = (Long) 99L;
        AdminUserStatusUpdateRequest request = new AdminUserStatusUpdateRequest(UserStatus.BLOCKED);

        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userAdminService.changeUserStatus(nonExistentUserId, request);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void whenUserExists_getUserById_shouldReturnUserResponse() {
        Long userId = (Long) 1L;
        User mockUser = new User();
        UserResponse mockResponse = new UserResponse(userId, "test@test.com","ACTIVE", null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userMapper.toDto(mockUser)).thenReturn(mockResponse);

        UserResponse result = userAdminService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.id());
        assertEquals("test@test.com", result.email());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void whenUserDoesNotExist_getUserById_shouldThrowResourceNotFoundException() {
        Long nonExistentUserId = (Long) 99L;

        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            userAdminService.getUserById(nonExistentUserId);
        });

        verify(userMapper, never()).toDto(any());
    }

    @Test
    void whenListAllUsers_shouldReturnPageOfUserResponses() {
        Pageable pageable = PageRequest.of(0, 5);
        User user1 = new User();
        User user2 = new User();
        List<User> userList = List.of(user1, user2);

        Page<User> userPage = new PageImpl<>(userList, pageable, userList.size());

        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<UserResponse> resultPage = userAdminService.listAllUsers(pageable);

        assertNotNull(resultPage);
        assertEquals(1, resultPage.getTotalPages());
        assertEquals(2, resultPage.getTotalElements());
        assertEquals(2, resultPage.getContent().size());

        verify(userRepository, times(1)).findAll(pageable);
        verify(userMapper, times(2)).toDto(any(User.class));
    }

    @Test
    void whenNoUsersExist_listAllUsers_shouldReturnEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<User> emptyUserPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(userRepository.findAll(pageable)).thenReturn(emptyUserPage);

        Page<UserResponse> resultPage = userAdminService.listAllUsers(pageable);

        assertNotNull(resultPage);
        assertTrue(resultPage.isEmpty());
        assertEquals(0, resultPage.getTotalElements());
        assertEquals(0, resultPage.getContent().size());
        verify(userRepository, times(1)).findAll(pageable);
        verify(userMapper, never()).toDto(any(User.class));
    }
}
