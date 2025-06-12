package com.banking.core_banking.domain.model.enums.product;

import lombok.Getter;

@Getter
public enum LoanStatus {
    ACTIVE("Active"),
    PAID_OFF("Paid off"),
    IN_DEFAULT("In default"),
    APPROVED("Approved"),
    REQUESTED("Requested");

    private final String displayName;

    LoanStatus(String displayName){
        this.displayName = displayName;
    }
}
