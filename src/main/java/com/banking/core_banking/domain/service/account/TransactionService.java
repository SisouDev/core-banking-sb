package com.banking.core_banking.domain.service.account;

import com.banking.core_banking.domain.model.dto.account.response.TransactionResponse;
import com.banking.core_banking.domain.model.dto.card.request.CreditPurchaseRequest;
import com.banking.core_banking.domain.model.dto.card.request.DebitPurchaseRequest;
import com.banking.core_banking.domain.model.entities.user.User;

public interface TransactionService {
    TransactionResponse performDebitPurchase(User loggedInUser, DebitPurchaseRequest request);
    void performCreditPurchase(User loggedInUser, CreditPurchaseRequest request);
}