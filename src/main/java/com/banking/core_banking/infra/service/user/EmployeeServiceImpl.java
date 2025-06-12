package com.banking.core_banking.infra.service.user;

import com.banking.core_banking.domain.model.dto.user.request.EmployeeCreateRequest;
import com.banking.core_banking.domain.model.dto.user.request.EmployeeUpdateRequest;
import com.banking.core_banking.domain.model.dto.user.response.EmployeeResponse;
import com.banking.core_banking.domain.model.entities.user.Employee;
import com.banking.core_banking.domain.model.entities.user.User;
import com.banking.core_banking.domain.model.utils.RegistrationCodeGenerator;
import com.banking.core_banking.domain.repository.user.EmployeeRepository;
import com.banking.core_banking.domain.repository.user.UserRepository;
import com.banking.core_banking.domain.service.user.EmployeeService;
import com.banking.core_banking.exceptions.others.BusinessException;
import com.banking.core_banking.exceptions.others.ResourceNotFoundException;
import com.banking.core_banking.infra.mapper.user.EmployeeMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegistrationCodeGenerator registrationCodeGenerator;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public EmployeeServiceImpl(
            EmployeeRepository employeeRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RegistrationCodeGenerator registrationCodeGenerator,
            EmployeeMapper employeeMapper
    ) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.registrationCodeGenerator = registrationCodeGenerator;
        this.employeeMapper = employeeMapper;
    }

    @Override
    @Transactional
    public EmployeeResponse createEmployee(EmployeeCreateRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email already in use.");
        }
        String registrationCode = registrationCodeGenerator.generateFor(request.role());
        String hashedPassword = passwordEncoder.encode(request.password());

        User newUser = User.createForEmployee(request.email(), hashedPassword, request.role());
        Employee newEmployee = Employee.create(
                request.firstName(),
                request.lastName(),
                registrationCode,
                newUser
        );

        Employee savedEmployee = employeeRepository.save(newEmployee);

        return employeeMapper.toDto(savedEmployee);
    }

    @Override
    public EmployeeResponse getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        return employeeMapper.toDto(employee);
    }

    @Override
    public Page<EmployeeResponse> getAllEmployees(Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findAll(pageable);
        return employeePage.map(employeeMapper::toDto);
    }

    @Override
    @Transactional
    public EmployeeResponse updateEmployee(Long employeeId, EmployeeUpdateRequest request) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        employeeMapper.updateFromDto(request, employee);
        Employee updatedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDto(updatedEmployee);
    }
}
