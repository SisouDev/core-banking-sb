package com.banking.core_banking.infra.mapper.user;

import com.banking.core_banking.domain.model.dto.user.request.BusinessCustomerCreateRequest;
import com.banking.core_banking.domain.model.dto.user.request.PersonalCustomerCreateRequest;
import com.banking.core_banking.domain.model.dto.user.response.BusinessCustomerResponse;
import com.banking.core_banking.domain.model.dto.user.response.CustomerResponse;
import com.banking.core_banking.domain.model.dto.user.response.CustomerSummaryResponse;
import com.banking.core_banking.domain.model.dto.user.response.PersonalCustomerResponse;
import com.banking.core_banking.domain.model.entities.user.BusinessCustomer;
import com.banking.core_banking.domain.model.entities.user.Customer;
import com.banking.core_banking.domain.model.entities.user.PersonalCustomer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public abstract class CustomerMapper {
    public CustomerResponse toDto(Customer customer) {
        if (customer instanceof PersonalCustomer personal) {
            return toPersonalDto(personal);
        }
        if (customer instanceof BusinessCustomer business) {
            return toBusinessDto(business);
        }
        throw new IllegalArgumentException("Unknown Customer type: " + customer.getClass().getName());
    }

    @Named("toSummaryDto")
    @Mapping(target = "customerType", expression = "java(customer instanceof PersonalCustomer ? \"PERSONAL\" : \"BUSINESS\")")
    public abstract CustomerSummaryResponse toSummaryDto(Customer customer);

    protected abstract PersonalCustomerResponse toPersonalDto(PersonalCustomer customer);
    protected abstract BusinessCustomerResponse toBusinessDto(BusinessCustomer customer);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract PersonalCustomer toEntity(PersonalCustomerCreateRequest dto);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract BusinessCustomer toEntity(BusinessCustomerCreateRequest dto);
}
