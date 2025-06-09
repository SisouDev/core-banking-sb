package com.banking.core_banking.domain.model.enums.user;

import lombok.Getter;

@Getter
public enum EmployeeStatus {
    ACTIVE("Active"),
    ON_LEAVE("On leave"),
    TERMINATED("Terminated");

    private final String displayName;

    EmployeeStatus(String displayName){
        this.displayName = displayName;
    }
}
