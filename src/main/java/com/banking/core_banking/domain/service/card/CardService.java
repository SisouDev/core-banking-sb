package com.banking.core_banking.domain.service.card;

import com.banking.core_banking.domain.model.dto.card.request.CardCreateRequest;
import com.banking.core_banking.domain.model.dto.card.request.CardStatusUpdateRequest;
import com.banking.core_banking.domain.model.dto.card.request.CreditFunctionActivateRequest;
import com.banking.core_banking.domain.model.dto.card.request.DebitFunctionActivateRequest;
import com.banking.core_banking.domain.model.dto.card.response.CardResponse;
import com.banking.core_banking.domain.model.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardService {
    CardResponse createCardForAccount(CardCreateRequest request);
    CardResponse getCardDetails(Long cardId);
    CardResponse activateDebitFunction(User loggedInUser, Long cardId, DebitFunctionActivateRequest request);
    CardResponse activateCreditFunction(User loggedInUser, Long cardId, CreditFunctionActivateRequest request);

    CardResponse updateCardStatus(User loggedInUser, Long cardId, CardStatusUpdateRequest request);
    CardResponse createCard(User loggedInUser, CardCreateRequest request);
    Page<CardResponse> getAllCardsForUser(User loggedInUser, Pageable pageable);
    CardResponse getCardDetails(User loggedInUser, Long cardId);
}
