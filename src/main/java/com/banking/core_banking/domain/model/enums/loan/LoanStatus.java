package com.banking.core_banking.domain.model.enums.loan;

import lombok.Getter;

@Getter
public enum LoanStatus {
    ACTIVE("Active"),
    PAID_OFF("Paid off"),
    IN_DEFAULT("In default");

    private final String displayName;

    LoanStatus(String displayName){
        this.displayName = displayName;
    }
}
