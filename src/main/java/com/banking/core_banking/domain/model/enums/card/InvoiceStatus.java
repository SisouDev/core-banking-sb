package com.banking.core_banking.domain.model.enums.card;

import lombok.Getter;

@Getter
public enum InvoiceStatus {
    OPEN("Open"),
    CLOSED("Closed"),
    PAID("Paid"),
    PARTIALLY_PAID("Partially paid"),
    OVERDUE("Overdue");

    private final String displayName;

    InvoiceStatus(String displayName){
        this.displayName = displayName;
    }
}
