package com.banking.core_banking.config;

import com.banking.core_banking.domain.model.entities.user.User;

public interface TokenProvider {
    String generateToken(User user);
    boolean validateToken(String token);
    Long getUserIdFromJWT(String token);
}
