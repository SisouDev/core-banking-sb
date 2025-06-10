package com.banking.core_banking.domain.model.dto.product.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "productType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = LoanProductResponse.class, name = "LOAN")
})
public interface BankingProductResponse {
    Long id();
    String name();
    String description();
    boolean isActive();
}
