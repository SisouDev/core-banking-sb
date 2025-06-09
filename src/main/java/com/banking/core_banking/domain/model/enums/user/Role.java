package com.banking.core_banking.domain.model.enums.user;

import lombok.Getter;

@Getter
public enum Role {
    ACCOUNT_MANAGER("Account Manager"),
    ANALYST("Analyst"),
    ADMIN("Administrator"),
    CUSTOMER("Customer");

    private final String displayName;

    Role(String displayName){
        this.displayName = displayName;
    }

}
