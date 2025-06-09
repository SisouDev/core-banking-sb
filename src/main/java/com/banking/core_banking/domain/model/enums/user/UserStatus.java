package com.banking.core_banking.domain.model.enums.user;

import lombok.Getter;

@Getter
public enum UserStatus {
    ACTIVE("Active"),
    PENDING_VERIFICATION("Pending verification"),
    BLOCKED("Blocked"),
    INACTIVE("Inactive");

    private final String displayName;

    UserStatus(String displayName){
        this.displayName = displayName;
    }

}
