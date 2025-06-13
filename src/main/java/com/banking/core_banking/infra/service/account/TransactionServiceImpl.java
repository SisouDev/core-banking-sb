package com.banking.core_banking.infra.service.account;

import com.banking.core_banking.domain.model.dto.account.request.WithdrawRequest;
import com.banking.core_banking.domain.model.dto.account.response.TransactionResponse;
import com.banking.core_banking.domain.model.dto.card.request.CreditPurchaseRequest;
import com.banking.core_banking.domain.model.dto.card.request.DebitPurchaseRequest;
import com.banking.core_banking.domain.model.entities.card.Card;
import com.banking.core_banking.domain.model.entities.card.CreditFunction;
import com.banking.core_banking.domain.model.entities.invoice.Invoice;
import com.banking.core_banking.domain.model.entities.user.User;
import com.banking.core_banking.domain.model.enums.card.InvoiceStatus;
import com.banking.core_banking.domain.repository.card.CardRepository;
import com.banking.core_banking.domain.repository.invoice.InvoiceRepository;
import com.banking.core_banking.domain.service.account.AccountService;
import com.banking.core_banking.domain.service.account.TransactionService;
import com.banking.core_banking.exceptions.others.BusinessException;
import com.banking.core_banking.exceptions.others.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final CardRepository cardRepository;
    private final AccountService accountService;
    private final InvoiceRepository invoiceRepository;

    public TransactionServiceImpl(CardRepository cardRepository, AccountService accountService, InvoiceRepository invoiceRepository) {
        this.cardRepository = cardRepository;
        this.accountService = accountService;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    @Transactional
    public TransactionResponse performDebitPurchase(User loggedInUser, DebitPurchaseRequest request) {
        Card card = cardRepository.findById(request.cardId())
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));

        if (!Objects.equals(card.getAccount().getCustomer().getUser().getId(), loggedInUser.getId())) {
            throw new BusinessException("Card does not belong to the authenticated user.");
        }

        card.validateForTransaction();
        if(!card.isDebit()){
            throw new BusinessException("Debit function is not active for this card.");
        }

        return accountService.withdraw(
                card.getAccount().getId(),
                new WithdrawRequest(request.amount(), "Purchase at " + request.merchantName())
        );
    }

    @Override
    @Transactional
    public void performCreditPurchase(User loggedInUser, CreditPurchaseRequest request) {
        Card card = cardRepository.findById(request.cardId())
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));

        if (!Objects.equals(card.getAccount().getCustomer().getUser().getId(), loggedInUser.getId())) {
            throw new BusinessException("Card does not belong to the authenticated user.");
        }
        card.validateForTransaction();
        if (!card.isCredit()) {
            throw new BusinessException("Credit function is not active for this card.");
        }

        CreditFunction creditFunction = card.getCreditFunction();
        creditFunction.validateLimit(request.amount());

        Invoice openInvoice = invoiceRepository.findByCreditFunctionIdAndStatus(creditFunction.getId(), InvoiceStatus.OPEN)
                .orElseGet(() -> {
                    YearMonth referenceMonth = YearMonth.now();
                    LocalDate today = LocalDate.now();
                    int closingDay = creditFunction.getInvoiceClosingDay();
                    LocalDate closingDate = today.withDayOfMonth(closingDay);
                    if (today.isAfter(closingDate)) {
                        closingDate = closingDate.plusMonths(1);
                    }
                    LocalDate dueDate = closingDate.plusDays(10);

                    Invoice newInvoice = Invoice.create(creditFunction, referenceMonth, closingDate, dueDate);
                    return invoiceRepository.save(newInvoice);
                });

        openInvoice.addItem(request.merchantName(), request.amount());
        creditFunction.useCredit(request.amount());
    }
}