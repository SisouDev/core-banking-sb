package com.banking.core_banking.domain.model.dto.user.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "customerType"
)
@JsonSubTypes({
        @Type(value = PersonalCustomerResponse.class, name = "PERSONAL"),
        @Type(value = BusinessCustomerResponse.class, name = "BUSINESS")
})
public interface CustomerResponse {
    Long id();
    String phone();
}
