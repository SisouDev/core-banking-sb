package com.banking.core_banking.infra.service.card;

import com.banking.core_banking.domain.model.dto.card.request.CardCreateRequest;
import com.banking.core_banking.domain.model.dto.card.request.CardStatusUpdateRequest;
import com.banking.core_banking.domain.model.dto.card.request.CreditFunctionActivateRequest;
import com.banking.core_banking.domain.model.dto.card.request.DebitFunctionActivateRequest;
import com.banking.core_banking.domain.model.dto.card.response.CardResponse;
import com.banking.core_banking.domain.model.entities.account.Account;
import com.banking.core_banking.domain.model.entities.card.Card;
import com.banking.core_banking.domain.repository.account.AccountRepository;
import com.banking.core_banking.domain.repository.card.CardRepository;
import com.banking.core_banking.domain.service.card.CardService;
import com.banking.core_banking.exceptions.others.ResourceNotFoundException;
import com.banking.core_banking.infra.mapper.card.CardMapper;
import com.banking.core_banking.infra.service.others.CardDetailsGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final CardMapper cardMapper;
    private final CardDetailsGenerator cardDetailsGenerator;

    @Autowired
    public CardServiceImpl(
            CardRepository cardRepository,
            AccountRepository accountRepository,
            CardMapper cardMapper,
            CardDetailsGenerator cardDetailsGenerator
    ) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
        this.cardMapper = cardMapper;
        this.cardDetailsGenerator = cardDetailsGenerator;
    }

    @Override
    @Transactional
    public CardResponse createCardForAccount(CardCreateRequest request) {
        Account account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        String cardNumber = cardDetailsGenerator.generateCardNumber();
        YearMonth expirationDate = cardDetailsGenerator.generateExpirationDate();

        String holderName = account.getCustomer().getDisplayName();

        Card newCard = Card.create(account, holderName, cardNumber, expirationDate);

        Card savedCard = cardRepository.save(newCard);
        return cardMapper.toDto(savedCard);
    }

    @Override
    public CardResponse getCardDetails(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));
        return cardMapper.toDto(card);
    }

    @Override
    @Transactional
    public CardResponse activateDebitFunction(Long cardId, DebitFunctionActivateRequest request) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));

        card.activateDebitFunction(request.dailyWithdrawalLimit(), request.dailyTransactionLimit());

        return cardMapper.toDto(card);
    }

    @Override
    @Transactional
    public CardResponse activateCreditFunction(Long cardId, CreditFunctionActivateRequest request) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));

        card.activateCreditFunction(request.creditLimit(), request.invoiceClosingDay());

        return cardMapper.toDto(card);
    }

    @Override
    @Transactional
    public void updateCardStatus(Long cardId, CardStatusUpdateRequest request) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));

        card.setStatus(request.newStatus());
    }
}
