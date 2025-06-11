package com.banking.core_banking.unit.service.others;

import com.banking.core_banking.config.JwtTokenProvider;
import com.banking.core_banking.domain.model.dto.admin.request.LoginRequest;
import com.banking.core_banking.domain.model.dto.admin.response.LoginResponse;
import com.banking.core_banking.domain.model.dto.user.request.PasswordChangeRequest;
import com.banking.core_banking.domain.model.dto.user.response.UserResponse;
import com.banking.core_banking.domain.model.entities.user.User;
import com.banking.core_banking.domain.repository.user.UserRepository;
import com.banking.core_banking.exceptions.others.ResourceNotFoundException;
import com.banking.core_banking.infra.mapper.user.UserMapper;
import com.banking.core_banking.infra.service.others.AuthServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenProvider tokenProvider;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    @Nested
    @DisplayName("Testes para o método login()")
    class LoginTests {

        @Test
        void whenCredentialsAreValid_shouldReturnLoginResponse() {
            LoginRequest loginRequest = new LoginRequest("user@test.com", "password123");
            User mockUser = new User();
            Authentication mockAuthentication = mock(Authentication.class);
            UserResponse mockUserResponse = new UserResponse(Long.valueOf(1), "user@test.com", "ACTIVE", null);
            String fakeToken = "fake.jwt.token";

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuthentication);
            when(mockAuthentication.getPrincipal()).thenReturn(mockUser);
            when(tokenProvider.generateToken(mockUser)).thenReturn(fakeToken);
            when(userMapper.toDto(mockUser)).thenReturn(mockUserResponse);

            LoginResponse response = authService.login(loginRequest);

            assertNotNull(response);
            assertEquals(fakeToken, response.accessToken());
            assertEquals("user@test.com", response.user().email());
        }

        @Test
        void whenCredentialsAreInvalid_shouldThrowBadCredentialsException() {
            LoginRequest loginRequest = new LoginRequest("user@test.com", "wrong-password");
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Invalid credentials"));

            assertThrows(BadCredentialsException.class, () -> {
                authService.login(loginRequest);
            });

            verify(tokenProvider, never()).generateToken(any());
        }
    }

    @Nested
    @DisplayName("Testes para o método changePassword()")
    class ChangePasswordTests {

        @Test
        void whenRequestIsValid_shouldChangePasswordSuccessfully() {

            Long userId = Long.valueOf(1);
            PasswordChangeRequest request = new PasswordChangeRequest("oldPass", "newPass123", "newPass123");
            User mockUser = new User();
            mockUser.setPassword("hashedOldPass");

            when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
            when(passwordEncoder.matches(eq("oldPass"), eq("hashedOldPass"))).thenReturn(Boolean.TRUE);
            when(passwordEncoder.encode("newPass123")).thenReturn("hashedNewPass");

            authService.changePassword(userId, request);

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository, times(1)).save(userCaptor.capture());

            assertEquals("hashedNewPass", userCaptor.getValue().getPassword());
        }

        @Test
        void whenUserNotFound_shouldThrowResourceNotFoundException() {
            when(userRepository.findById(Long.valueOf(anyLong()))).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> {
                authService.changePassword(Long.valueOf(99L), new PasswordChangeRequest("a", "b", "b"));
            });
        }

        @Test
        void whenOldPasswordIsIncorrect_shouldThrowBadCredentialsException() {
            Long userId = (Long) 1L;
            PasswordChangeRequest request = new PasswordChangeRequest("wrongOldPass", "newPass123", "newPass123");
            User mockUser = new User();
            mockUser.setPassword("hashedOldPass");

            when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
            when(passwordEncoder.matches(eq("wrongOldPass"), eq("hashedOldPass"))).thenReturn(Boolean.valueOf(false));

            assertThrows(BadCredentialsException.class, () -> {
                authService.changePassword(userId, request);
            });

            verify(userRepository, never()).save(any());
        }
    }
}