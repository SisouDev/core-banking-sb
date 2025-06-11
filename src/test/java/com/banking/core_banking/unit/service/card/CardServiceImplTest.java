package com.banking.core_banking.unit.service.card;

import com.banking.core_banking.domain.model.dto.card.request.CardCreateRequest;
import com.banking.core_banking.domain.model.dto.card.request.CardStatusUpdateRequest;
import com.banking.core_banking.domain.model.dto.card.request.DebitFunctionActivateRequest;
import com.banking.core_banking.domain.model.dto.card.response.CardResponse;
import com.banking.core_banking.domain.model.entities.account.Account;
import com.banking.core_banking.domain.model.entities.card.Card;
import com.banking.core_banking.domain.model.entities.user.Customer;
import com.banking.core_banking.domain.model.enums.card.CardStatus;
import com.banking.core_banking.domain.repository.account.AccountRepository;
import com.banking.core_banking.domain.repository.card.CardRepository;
import com.banking.core_banking.exceptions.others.ResourceNotFoundException;
import com.banking.core_banking.infra.mapper.card.CardMapper;
import com.banking.core_banking.infra.service.card.CardServiceImpl;
import com.banking.core_banking.infra.service.others.CardDetailsGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CardMapper cardMapper;
    @Mock
    private CardDetailsGenerator cardDetailsGenerator;

    @InjectMocks
    private CardServiceImpl cardService;

    @Nested
    @DisplayName("Testes para createCardForAccount()")
    class CreateCardTests {

        @Test
        void whenAccountExists_shouldCreateCardSuccessfully() {
            var request = new CardCreateRequest(Long.valueOf(1));
            var mockAccount = mock(Account.class);
            var mockCustomer = mock(Customer.class);
            var mockCardEntity = new Card();
            var expectedResponse = new CardResponse(Long.valueOf(10), "**** **** **** 5678", "John Doe", YearMonth.of(2030, 12), "INACTIVE", null, null);

            when(accountRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(mockAccount));
            when(mockAccount.getCustomer()).thenReturn(mockCustomer);
            when(mockCustomer.getDisplayName()).thenReturn("John Doe");
            when(cardDetailsGenerator.generateCardNumber()).thenReturn("1234567812345678");
            when(cardDetailsGenerator.generateExpirationDate()).thenReturn(YearMonth.of(2030, 12));

            when(cardRepository.save(any(Card.class))).thenReturn(mockCardEntity);

            when(cardMapper.toDto(mockCardEntity)).thenReturn(expectedResponse);

            CardResponse actualResponse = cardService.createCardForAccount(request);

            assertNotNull(actualResponse);
            assertEquals(10L, actualResponse.id());
            assertEquals("**** **** **** 5678", actualResponse.maskedNumber());
            assertEquals("John Doe", actualResponse.holderName());

            verify(cardRepository, times(1)).save(any(Card.class));
        }

        @Test
        void whenAccountNotFound_shouldThrowResourceNotFoundException() {
            var request = new CardCreateRequest(Long.valueOf(99));
            when(accountRepository.findById(Long.valueOf(99))).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> {
                cardService.createCardForAccount(request);
            });
            verify(cardRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Testes para activateDebitFunction()")
    class ActivateDebitTests {

        @Test
        void whenCardExists_shouldActivateDebitFunction() {
            var request = new DebitFunctionActivateRequest(new BigDecimal("1000"), new BigDecimal("5000"));
            var mockCard = mock(Card.class);

            when(cardRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(mockCard));

            cardService.activateDebitFunction(Long.valueOf(1), request);

            verify(mockCard, times(1)).activateDebitFunction(request.dailyWithdrawalLimit(), request.dailyTransactionLimit());
        }
    }

    @Nested
    @DisplayName("Testes para updateCardStatus()")
    class UpdateStatusTests {

        @Test
        void whenCardExists_shouldUpdateStatus() {
            var request = new CardStatusUpdateRequest(CardStatus.BLOCKED);
            var mockCard = mock(Card.class);

            when(cardRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(mockCard));

            cardService.updateCardStatus(Long.valueOf(1), request);

            verify(mockCard, times(1)).setStatus(CardStatus.BLOCKED);
        }
    }
}