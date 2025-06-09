package com.banking.core_banking.domain.model.enums.account;

import lombok.Getter;

@Getter
public enum TransactionType {
    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal"),
    TRANSFER_SENT("Transfer sent"),
    TRANSFER_RECEIVED("Transfer received"),
    DEBIT_CARD_PURCHASE("Debit card purchase"),
    CREDIT_CARD_BILL_PAYMENT("Credit card bill payment"),
    LOAN_DISBURSEMENT("Loan disbursement"),
    LOAN_INSTALLMENT_PAYMENT("Loan installment payment"),
    INVESTMENT_PURCHASE("Investment purchase"),
    INVESTMENT_REDEMPTION("Investment redemption"),
    INSURANCE_PREMIUM_PAYMENT("Insurance premium payment");

    private final String displayName;

    TransactionType(String displayName){
        this.displayName = displayName;
    }
}
