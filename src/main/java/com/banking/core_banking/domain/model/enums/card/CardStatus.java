package com.banking.core_banking.domain.model.enums.card;

import lombok.Getter;

@Getter
public enum CardStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    BLOCKED("Blocked"),
    CANCELED("Canceled"),
    EXPIRED("Expired");

    private final String displayName;

    CardStatus(String displayName){
        this.displayName = displayName;
    }
}
