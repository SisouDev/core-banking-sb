package com.banking.core_banking.domain.model.enums.product;

import lombok.Getter;

@Getter
public enum InstallmentStatus {
    PENDING("Pending"),
    PAID("Paid"),
    OVERDUE("Overdue");

    private final String displayName;

    InstallmentStatus(String displayName){
        this.displayName = displayName;
    }
}
