package com.banking.core_banking.domain.controller.others;

import com.banking.core_banking.config.TokenProvider;
import com.banking.core_banking.domain.model.dto.admin.request.LoginRequest;
import com.banking.core_banking.domain.model.dto.admin.response.LoginResponse;
import com.banking.core_banking.domain.model.dto.user.response.UserResponse; // Importe o UserResponse
import com.banking.core_banking.domain.model.entities.user.User;
import com.banking.core_banking.infra.mapper.user.UserMapper; // Importe o UserMapper
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserMapper userMapper;

    @Value("${app.jwt.expiration-ms}")
    private Long expiresIn;

    public AuthController(AuthenticationManager authenticationManager, TokenProvider tokenProvider, UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userMapper = userMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        User user = (User) authentication.getPrincipal();
        String token = tokenProvider.generateToken(user);

        UserResponse userResponse = userMapper.toDto(user);

        LoginResponse loginResponse = new LoginResponse(token, expiresIn, userResponse);

        return ResponseEntity.ok(loginResponse);
    }
}