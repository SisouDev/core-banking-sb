package com.banking.core_banking.unit.service.user;

import com.banking.core_banking.domain.model.entities.user.User;
import com.banking.core_banking.domain.repository.user.UserRepository;
import com.banking.core_banking.infra.service.user.UserDetailsServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("Deve retornar UserDetails quando o usuário for encontrado pelo email")
    void whenUserExists_loadUserByUsername_shouldReturnUserDetails() {
        String email = "user@test.com";
        User mockUser = new User();
        mockUser.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
    }


    @Test
    @DisplayName("Deve lançar UsernameNotFoundException quando o usuário não for encontrado")
    void whenUserDoesNotExist_loadUserByUsername_shouldThrowUsernameNotFoundException() {
        String nonExistentEmail = "notfound@test.com";

        when(userRepository.findByEmail(nonExistentEmail)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(nonExistentEmail);
        });

        assertTrue(exception.getMessage().contains(nonExistentEmail));
    }
}
