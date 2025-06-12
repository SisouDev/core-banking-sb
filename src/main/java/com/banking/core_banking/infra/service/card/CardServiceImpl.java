package com.banking.core_banking.infra.service.card;

import com.banking.core_banking.domain.model.dto.card.request.CardCreateRequest;
import com.banking.core_banking.domain.model.dto.card.request.CardStatusUpdateRequest;
import com.banking.core_banking.domain.model.dto.card.request.CreditFunctionActivateRequest;
import com.banking.core_banking.domain.model.dto.card.request.DebitFunctionActivateRequest;
import com.banking.core_banking.domain.model.dto.card.response.CardResponse;
import com.banking.core_banking.domain.model.entities.account.Account;
import com.banking.core_banking.domain.model.entities.card.Card;
import com.banking.core_banking.domain.model.entities.user.Customer;
import com.banking.core_banking.domain.model.entities.user.User;
import com.banking.core_banking.domain.model.utils.CardNumberGenerator;
import com.banking.core_banking.domain.repository.account.AccountRepository;
import com.banking.core_banking.domain.repository.card.CardRepository;
import com.banking.core_banking.domain.repository.user.CustomerRepository;
import com.banking.core_banking.domain.service.card.CardService;
import com.banking.core_banking.exceptions.others.BusinessException;
import com.banking.core_banking.exceptions.others.ResourceNotFoundException;
import com.banking.core_banking.infra.mapper.card.CardMapper;
import com.banking.core_banking.infra.service.others.CardDetailsGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.Objects;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final CardMapper cardMapper;
    private final CardDetailsGenerator cardDetailsGenerator;
    private final CustomerRepository customerRepository;

    @Autowired
    public CardServiceImpl(
            CardRepository cardRepository,
            AccountRepository accountRepository,
            CardMapper cardMapper,
            CardDetailsGenerator cardDetailsGenerator, CustomerRepository customerRepository
    ) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
        this.cardMapper = cardMapper;
        this.cardDetailsGenerator = cardDetailsGenerator;
        this.customerRepository = customerRepository;
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

    @Override
    @Transactional
    public CardResponse createCard(User loggedInUser, CardCreateRequest request) {
        Customer customer = customerRepository.findByUserId(loggedInUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found for the logged-in user."));

        Account account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + request.accountId()));

        if (!Objects.equals(account.getCustomer().getId(), customer.getId())) {
            throw new BusinessException("Account does not belong to the authenticated user.");
        }

        String holderName = account.getCustomer().getDisplayName();

        Card newCard = Card.create(
                account,
                holderName,
                CardNumberGenerator.generateCardNumber(),
                CardNumberGenerator.generateExpirationDate()
        );


        Card savedCard = cardRepository.save(newCard);
        return cardMapper.toDto(savedCard);
    }

    @Override
    public Page<CardResponse> getAllCardsForUser(User loggedInUser, Pageable pageable) {
        Customer customer = customerRepository.findByUserId(loggedInUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found for the logged-in user."));

        Page<Card> cards = cardRepository.findByCustomerId(customer.getId(), pageable);
        return cards.map(cardMapper::toDto);
    }

    @Override
    public CardResponse getCardDetails(User loggedInUser, Long cardId) {
        Customer customer = customerRepository.findByUserId(loggedInUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found for the logged-in user."));

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + cardId));

        if (!Objects.equals(card.getAccount().getCustomer().getId(), customer.getId())) {
            throw new BusinessException("Card does not belong to the authenticated user.");
        }

        return cardMapper.toDto(card);
    }
}
