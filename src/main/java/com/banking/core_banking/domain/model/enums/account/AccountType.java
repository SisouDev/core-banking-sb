package com.banking.core_banking.domain.model.enums.account;

import lombok.Getter;

@Getter
public enum AccountType {
    CHECKING_ACCOUNT("CHECKING"),
    SAVINGS_ACCOUNT("SAVINGS");

    private final String displayName;

    AccountType(String displayName){
        this.displayName = displayName;
    }
}
