package com.banking.core_banking.domain.controller.user;

import com.banking.core_banking.domain.model.dto.user.request.EmployeeCreateRequest;
import com.banking.core_banking.domain.model.dto.user.request.EmployeeUpdateRequest;
import com.banking.core_banking.domain.model.dto.user.response.EmployeeResponse;
import com.banking.core_banking.domain.service.user.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/employees")
@PreAuthorize("hasRole('ADMIN')")
public class EmployeeAdminController {

    private final EmployeeService employeeService;

    public EmployeeAdminController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeCreateRequest request) {
        EmployeeResponse response = employeeService.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeResponse>> getAllEmployees(Pageable pageable) {
        Page<EmployeeResponse> response = employeeService.getAllEmployees(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long employeeId) {
        EmployeeResponse response = employeeService.getEmployeeById(employeeId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeeUpdateRequest request
    ) {
        EmployeeResponse response = employeeService.updateEmployee(employeeId, request);
        return ResponseEntity.ok(response);
    }
}