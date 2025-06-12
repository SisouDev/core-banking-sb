package com.banking.core_banking.unit.service.user;

import com.banking.core_banking.domain.model.dto.user.request.EmployeeCreateRequest;
import com.banking.core_banking.domain.model.dto.user.request.EmployeeUpdateRequest;
import com.banking.core_banking.domain.model.dto.user.response.EmployeeResponse;
import com.banking.core_banking.domain.model.entities.user.Employee;
import com.banking.core_banking.domain.model.enums.user.Role;
import com.banking.core_banking.domain.model.utils.RegistrationCodeGenerator;
import com.banking.core_banking.domain.repository.user.EmployeeRepository;
import com.banking.core_banking.domain.repository.user.UserRepository;
import com.banking.core_banking.exceptions.others.BusinessException;
import com.banking.core_banking.exceptions.others.ResourceNotFoundException;
import com.banking.core_banking.infra.mapper.user.EmployeeMapper;
import com.banking.core_banking.infra.service.user.EmployeeServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RegistrationCodeGenerator registrationCodeGenerator;
    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Nested
    @DisplayName("Testes para createEmployee()")
    class CreateEmployeeTests {

        @Test
        void whenRequestIsValid_shouldCreateEmployeeAndUserSuccessfully() {
            var request = new EmployeeCreateRequest("John", "Doe", "john.doe@bank.com", "password123", Role.ANALYST);
            var savedEmployee = mock(Employee.class);
            var expectedResponse = mock(EmployeeResponse.class);

            when(userRepository.existsByEmail(request.email())).thenReturn(false);
            when(registrationCodeGenerator.generateFor(request.role())).thenReturn("AN2506110001");
            when(passwordEncoder.encode(request.password())).thenReturn("hashedPassword123");
            when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);
            when(employeeMapper.toDto(savedEmployee)).thenReturn(expectedResponse);

            EmployeeResponse actualResponse = employeeService.createEmployee(request);

            assertNotNull(actualResponse);
            assertEquals(expectedResponse, actualResponse);

            ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
            verify(employeeRepository).save(employeeCaptor.capture());

            Employee capturedEmployee = employeeCaptor.getValue();
            assertEquals("John", capturedEmployee.getFirstName());
            assertEquals("hashedPassword123", capturedEmployee.getUser().getPassword());
            assertEquals(Role.ANALYST, capturedEmployee.getUser().getRole());
        }

        @Test
        void whenEmailAlreadyExists_shouldThrowBusinessException() {
            var request = new EmployeeCreateRequest("John", "Doe", "john.doe@bank.com", "password123", Role.ANALYST);

            when(userRepository.existsByEmail(request.email())).thenReturn(true);

            assertThrows(BusinessException.class, () -> {
                employeeService.createEmployee(request);
            });

            verify(employeeRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Testes para updateEmployee()")
    class UpdateEmployeeTests {

        @Test
        void whenEmployeeExists_shouldUpdateAndReturnResponse() {
            Long employeeId = 1L;
            var request = new EmployeeUpdateRequest("Jonathan", "Doe");
            var mockEmployee = new Employee();

            when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(mockEmployee));

            doNothing().when(employeeMapper).updateFromDto(any(), any());
            when(employeeRepository.save(mockEmployee)).thenReturn(mockEmployee);

            employeeService.updateEmployee(employeeId, request);

            verify(employeeMapper, times(1)).updateFromDto(request, mockEmployee);
            verify(employeeRepository, times(1)).save(mockEmployee);
        }

        @Test
        void whenUpdatingNonExistentEmployee_shouldThrowResourceNotFoundException() {
            Long employeeId = 99L;
            var request = new EmployeeUpdateRequest("Jonathan", "Doe");
            when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> {
                employeeService.updateEmployee(employeeId, request);
            });
        }
    }
}