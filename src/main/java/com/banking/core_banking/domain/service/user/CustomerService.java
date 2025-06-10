package com.banking.core_banking.domain.service.user;

import com.banking.core_banking.domain.model.dto.user.request.BusinessCustomerCreateRequest;
import com.banking.core_banking.domain.model.dto.user.request.PersonalCustomerCreateRequest;
import com.banking.core_banking.domain.model.dto.user.request.UserUpdateRequest;
import com.banking.core_banking.domain.model.dto.user.response.CustomerResponse;

public interface CustomerService {
    CustomerResponse createBusinessCustomer(BusinessCustomerCreateRequest request);

    CustomerResponse createPersonalCustomer(PersonalCustomerCreateRequest request);

    CustomerResponse getCustomerById(Long customerId);

    CustomerResponse updateCustomerProfile(Long customerId, UserUpdateRequest request);
}
