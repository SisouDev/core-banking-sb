package com.banking.core_banking.domain.model.enums.user;

import lombok.Getter;

@Getter
public enum Role {
    ACCOUNT_MANAGER("Account Manager", "AM"),
    ANALYST("Analyst", "AN"),
    ADMIN("Administrator", "AD"),
    CUSTOMER("Customer", "CS");

    private final String displayName;
    private final String abbreviation;

    Role(String displayName, String abbreviation){
        this.displayName = displayName;
        this.abbreviation = abbreviation;
    }

}
