package com.banking.core_banking.domain.controller.user;

import com.banking.core_banking.domain.model.dto.user.request.BusinessCustomerCreateRequest;
import com.banking.core_banking.domain.model.dto.user.request.PersonalCustomerCreateRequest;
import com.banking.core_banking.domain.model.dto.user.response.CustomerResponse;
import com.banking.core_banking.domain.service.user.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/personal")
    public ResponseEntity<CustomerResponse> createPersonalCustomer(@Valid @RequestBody PersonalCustomerCreateRequest request) {
        CustomerResponse response = customerService.createPersonalCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/business")
    public ResponseEntity<CustomerResponse> createBusinessCustomer(@Valid @RequestBody BusinessCustomerCreateRequest request) {
        CustomerResponse response = customerService.createBusinessCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        CustomerResponse response = customerService.getCustomerById(id);
        return ResponseEntity.ok(response);
    }
}