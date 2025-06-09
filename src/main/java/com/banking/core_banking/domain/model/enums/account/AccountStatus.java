package com.banking.core_banking.domain.model.enums.account;

import lombok.Getter;

@Getter
public enum AccountStatus {
    ACTIVE("Active"),
    BLOCKED("Blocked"),
    CLOSED("Closed");

    private final String displayName;

    AccountStatus(String displayName){
        this.displayName = displayName;
    }
}
