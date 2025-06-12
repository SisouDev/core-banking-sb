package com.banking.core_banking.unit.service.invoice;

import com.banking.core_banking.domain.model.dto.account.request.WithdrawRequest;
import com.banking.core_banking.domain.model.dto.account.response.TransactionResponse;
import com.banking.core_banking.domain.model.dto.invoice.request.InvoicePaymentRequest;
import com.banking.core_banking.domain.model.entities.account.Account;
import com.banking.core_banking.domain.model.entities.card.Card;
import com.banking.core_banking.domain.model.entities.card.CreditFunction;
import com.banking.core_banking.domain.model.entities.invoice.Invoice;
import com.banking.core_banking.domain.model.enums.card.InvoiceStatus;
import com.banking.core_banking.domain.repository.card.CardRepository;
import com.banking.core_banking.domain.repository.invoice.InvoiceRepository;
import com.banking.core_banking.domain.service.account.AccountService;
import com.banking.core_banking.exceptions.invoice.InvoiceAlreadyPaidException;
import com.banking.core_banking.exceptions.others.BusinessException;
import com.banking.core_banking.exceptions.others.ResourceNotFoundException;
import com.banking.core_banking.infra.mapper.invoice.InvoiceMapper;
import com.banking.core_banking.infra.service.invoice.InvoiceServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private AccountService accountService;
    @Mock
    private InvoiceMapper invoiceMapper;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @Nested
    @DisplayName("Testes para getInvoiceDetails()")
    class GetInvoiceDetailsTests {

        @Test
        void whenInvoiceExists_shouldReturnDetailsDto() {
            var mockInvoice = mock(Invoice.class);
            when(invoiceRepository.findById(anyLong())).thenReturn(Optional.of(mockInvoice));

            invoiceService.getInvoiceDetails(Long.valueOf(1));

            verify(invoiceMapper, times(1)).toDetailsDto(mockInvoice);
        }

        @Test
        void whenInvoiceDoesNotExist_shouldThrowResourceNotFoundException() {
            when(invoiceRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> {
                invoiceService.getInvoiceDetails(Long.valueOf(99));
            });
        }
    }

    @Nested
    @DisplayName("Testes para findInvoicesByCardId()")
    class FindInvoicesByCardIdTests {

        @Test
        void whenCardDoesNotExist_shouldThrowResourceNotFoundException() {
            when(cardRepository.existsById(anyLong())).thenReturn(false);

            assertThrows(ResourceNotFoundException.class, () -> {
                invoiceService.findInvoicesByCardId(Long.valueOf(99), mock(Pageable.class));
            });

            verify(invoiceRepository, never()).findByCreditFunctionCardIdOrderByReferenceMonthDesc(anyLong(), any());
        }
    }

    @Nested
    @DisplayName("Testes para payInvoice()")
    class PayInvoiceTests {

        private Invoice mockInvoice;
        private CreditFunction mockCreditFunction;

        void setupHappyPathMocks(InvoiceStatus initialStatus, BigDecimal totalAmount) {
            mockInvoice = mock(Invoice.class);
            mockCreditFunction = mock(CreditFunction.class);
            var mockCard = mock(Card.class);
            var mockAccount = mock(Account.class);

            when(mockInvoice.getStatus()).thenReturn(initialStatus);
            when(mockInvoice.getTotalAmount()).thenReturn(totalAmount);
            when(mockInvoice.getCreditFunction()).thenReturn(mockCreditFunction);
            when(mockCreditFunction.getCard()).thenReturn(mockCard);
            when(mockCard.getAccount()).thenReturn(mockAccount);
            when(mockAccount.getId()).thenReturn(Long.valueOf(10));

            when(invoiceRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(mockInvoice));
        }

        @Test
        @DisplayName("Deve pagar a fatura com sucesso quando o pagamento é válido")
        void whenPaymentIsValid_shouldSucceed() {
            Long invoiceId = Long.valueOf(1);
            BigDecimal paymentAmount = new BigDecimal("1250.75");
            var request = new InvoicePaymentRequest(paymentAmount);

            var mockAccount = mock(Account.class);
            var mockCard = mock(Card.class);
            var mockCreditFunction = mock(CreditFunction.class);
            var mockInvoice = mock(Invoice.class);

            when(mockAccount.getId()).thenReturn(Long.valueOf(10));
            when(mockCard.getAccount()).thenReturn(mockAccount);
            when(mockCreditFunction.getCard()).thenReturn(mockCard);
            when(mockInvoice.getCreditFunction()).thenReturn(mockCreditFunction);

            when(mockInvoice.getStatus()).thenReturn(InvoiceStatus.CLOSED);
            when(mockInvoice.getTotalAmount()).thenReturn(paymentAmount);

            when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(mockInvoice));

            when(accountService.withdraw(anyLong(), any(WithdrawRequest.class)))
                    .thenReturn(mock(TransactionResponse.class));

            invoiceService.payInvoice(invoiceId, request);

            ArgumentCaptor<WithdrawRequest> withdrawRequestCaptor = ArgumentCaptor.forClass(WithdrawRequest.class);
            verify(accountService, times(1)).withdraw(eq(Long.valueOf(10)), withdrawRequestCaptor.capture());
            assertEquals(paymentAmount, withdrawRequestCaptor.getValue().amount());

            verify(mockInvoice, times(1)).markAsPaid();
            verify(mockCreditFunction, times(1)).applyPayment(paymentAmount);
        }

        @Test
        void whenInvoiceIsAlreadyPaid_shouldThrowInvoiceAlreadyPaidException() {
            var request = new InvoicePaymentRequest(new BigDecimal("500.00"));
            var mockInvoice = mock(Invoice.class);

            when(mockInvoice.getStatus()).thenReturn(InvoiceStatus.PAID);
            when(invoiceRepository.findById(anyLong())).thenReturn(Optional.of(mockInvoice));

            assertThrows(InvoiceAlreadyPaidException.class, () -> {
                invoiceService.payInvoice(Long.valueOf(1), request);
            });

            verify(accountService, never()).withdraw(any(), any());
        }

        @Test
        void whenPaymentAmountIsDifferent_shouldThrowBusinessException() {
            var request = new InvoicePaymentRequest(new BigDecimal("499.00"));
            var mockInvoice = mock(Invoice.class);

            when(mockInvoice.getStatus()).thenReturn(InvoiceStatus.CLOSED);
            when(mockInvoice.getTotalAmount()).thenReturn(new BigDecimal("500.00"));
            when(invoiceRepository.findById(anyLong())).thenReturn(Optional.of(mockInvoice));

            assertThrows(BusinessException.class, () -> {
                invoiceService.payInvoice(Long.valueOf(1), request);
            });

            verify(accountService, never()).withdraw(any(), any());
        }
    }
}