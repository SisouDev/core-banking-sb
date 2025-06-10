package com.banking.core_banking.domain.service.user;

import com.banking.core_banking.domain.model.dto.user.request.EmployeeCreateRequest;
import com.banking.core_banking.domain.model.dto.user.request.EmployeeUpdateRequest;
import com.banking.core_banking.domain.model.dto.user.response.EmployeeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    EmployeeResponse createEmployee(EmployeeCreateRequest request);
    EmployeeResponse getEmployeeById(Long employeeId);
    Page<EmployeeResponse> getAllEmployees(Pageable pageable);
    EmployeeResponse updateEmployee(Long employeeId, EmployeeUpdateRequest request);
}
