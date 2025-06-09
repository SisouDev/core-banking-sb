package com.banking.core_banking.domain.model.enums.account;

import lombok.Getter;

@Getter
public enum AccountType {
    CHECKING_ACCOUNT("Checking account"),
    SAVINGS_ACCOUNT("Savings account");

    private final String displayName;

    AccountType(String displayName){
        this.displayName = displayName;
    }
}
