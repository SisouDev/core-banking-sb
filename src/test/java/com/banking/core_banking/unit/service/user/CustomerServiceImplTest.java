package com.banking.core_banking.unit.service.user;

import com.banking.core_banking.domain.model.dto.user.request.AddressCreateRequest;
import com.banking.core_banking.domain.model.dto.user.request.PersonalCustomerCreateRequest;
import com.banking.core_banking.domain.model.dto.user.request.UserUpdateRequest;
import com.banking.core_banking.domain.model.dto.user.response.CustomerResponse;
import com.banking.core_banking.domain.model.dto.user.response.PersonalCustomerResponse;
import com.banking.core_banking.domain.model.entities.user.Customer;
import com.banking.core_banking.domain.model.entities.user.PersonalCustomer;
import com.banking.core_banking.domain.model.entities.user.User;
import com.banking.core_banking.domain.repository.user.CustomerRepository;
import com.banking.core_banking.domain.repository.user.UserRepository;
import com.banking.core_banking.exceptions.user.EmailAlreadyExistsException;
import com.banking.core_banking.infra.mapper.user.CustomerMapper;
import com.banking.core_banking.infra.service.user.CustomerServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Nested
    @DisplayName("Testes para createPersonalCustomer()")
    class CreatePersonalCustomerTests {

        @Test
        void whenRequestIsValid_shouldCreatePersonalCustomer() {
            AddressCreateRequest mockAddressRequest = new AddressCreateRequest(
                    "Rua dos Testes", "123", null, "Bairro Mock",
                    "Test City", "SP", "12345-678", "BR"
            );
            var request = new PersonalCustomerCreateRequest(
                    "test@test.com", "password123", "John Doe", "12345678900", LocalDate.now().minusYears(20), null, mockAddressRequest
            );
            var mockCustomerEntity = mock(PersonalCustomer.class);
            var savedCustomerEntity = mock(PersonalCustomer.class);

            var expectedResponse = mock(PersonalCustomerResponse.class);

            when(userRepository.existsByEmail(request.email())).thenReturn(false);
            when(passwordEncoder.encode(request.password())).thenReturn("anyHashedPassword");
            when(customerMapper.toEntity(request)).thenReturn(mockCustomerEntity);
            when(customerRepository.save(mockCustomerEntity)).thenReturn(savedCustomerEntity);

            when(customerMapper.toDto(savedCustomerEntity)).thenReturn(expectedResponse);

            CustomerResponse actualResponse = customerService.createPersonalCustomer(request);

            assertNotNull(actualResponse);
            assertEquals(expectedResponse, actualResponse);

            verify(customerRepository, times(1)).save(any(PersonalCustomer.class));
        }

        @Test
        void whenEmailAlreadyExists_shouldThrowEmailAlreadyExistsException() {
            AddressCreateRequest mockAddressRequest = new AddressCreateRequest(
                    "Rua dos Testes", "123", null, "Bairro Mock",
                    "Test City", "SP", "12345-678", "BR"
            );
            var request = new PersonalCustomerCreateRequest("test@test.com", "password123", "John Doe", "12345678900", null, null,mockAddressRequest);
            when(userRepository.existsByEmail(request.email())).thenReturn(true);

            assertThrows(EmailAlreadyExistsException.class, () -> {
                customerService.createPersonalCustomer(request);
            });
            verify(customerRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Testes para updateCustomerProfile()")
    class UpdateCustomerProfileTests {

        @Test
        void whenUpdateEmailIsValid_shouldUpdateSuccessfully() {
            Long customerId = 1L;
            var request = new UserUpdateRequest("new.email@test.com");
            var mockUser = mock(User.class);
            var mockCustomer = mock(Customer.class);

            when(mockCustomer.getUser()).thenReturn(mockUser);
            when(customerRepository.findById(customerId)).thenReturn(Optional.of(mockCustomer));
            when(userRepository.existsByEmail(request.email())).thenReturn(false);

            customerService.updateCustomerProfile(customerId, request);

            verify(mockUser, times(1)).setEmail("new.email@test.com");
        }

        @Test
        void whenNewEmailAlreadyExists_shouldThrowEmailAlreadyExistsException() {
            Long customerId = 1L;
            var request = new UserUpdateRequest("existing.email@test.com");
            var mockCustomer = mock(Customer.class);
            var mockUser = mock(User.class);

            when(mockCustomer.getUser()).thenReturn(mockUser);
            when(customerRepository.findById(customerId)).thenReturn(Optional.of(mockCustomer));
            when(userRepository.existsByEmail(request.email())).thenReturn(true);

            assertThrows(EmailAlreadyExistsException.class, () -> {
                customerService.updateCustomerProfile(customerId, request);
            });
            verify(mockUser, never()).setEmail(any());
        }
    }
}