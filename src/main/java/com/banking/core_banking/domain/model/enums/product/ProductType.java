package com.banking.core_banking.domain.model.enums.product;

import lombok.Getter;

@Getter
public enum ProductType {
    LOAN("Loan");

    private final String displayName;

    ProductType(String displayName){
        this.displayName = displayName;
    }
}
