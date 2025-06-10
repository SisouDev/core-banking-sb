package com.banking.core_banking.domain.service.card;

import com.banking.core_banking.domain.model.dto.card.request.CardCreateRequest;
import com.banking.core_banking.domain.model.dto.card.request.CardStatusUpdateRequest;
import com.banking.core_banking.domain.model.dto.card.request.CreditFunctionActivateRequest;
import com.banking.core_banking.domain.model.dto.card.request.DebitFunctionActivateRequest;
import com.banking.core_banking.domain.model.dto.card.response.CardResponse;

public interface CardService {
    CardResponse createCardForAccount(CardCreateRequest request);
    CardResponse getCardDetails(Long cardId);
    CardResponse activateDebitFunction(Long cardId, DebitFunctionActivateRequest request);
    CardResponse activateCreditFunction(Long cardId, CreditFunctionActivateRequest request);

    void updateCardStatus(Long cardId, CardStatusUpdateRequest request);
}
