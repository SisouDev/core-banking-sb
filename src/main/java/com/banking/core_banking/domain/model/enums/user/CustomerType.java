package com.banking.core_banking.domain.model.enums.user;

import lombok.Getter;

@Getter
public enum CustomerType {
    PERSONAL("Personal"),
    BUSINESS("Business");

    private final String displayName;

    CustomerType(String displayName){
        this.displayName = displayName;
    }
}
