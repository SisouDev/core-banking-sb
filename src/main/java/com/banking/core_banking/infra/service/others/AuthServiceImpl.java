package com.banking.core_banking.infra.service.others;

import com.banking.core_banking.config.JwtTokenProvider;
import com.banking.core_banking.domain.model.dto.admin.request.LoginRequest;
import com.banking.core_banking.domain.model.dto.admin.response.LoginResponse;
import com.banking.core_banking.domain.model.entities.user.User;
import com.banking.core_banking.domain.service.others.AuthService;
import com.banking.core_banking.infra.mapper.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserMapper userMapper;

    @Autowired
    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            JwtTokenProvider tokenProvider,
            UserMapper userMapper
    ) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userMapper = userMapper;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = (User) authentication.getPrincipal();
        String accessToken = tokenProvider.generateToken(user);

        return new LoginResponse(
                accessToken,
                3600L,
                userMapper.toDto(user)
        );
    }
}
