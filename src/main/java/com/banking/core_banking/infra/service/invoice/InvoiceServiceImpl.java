package com.banking.core_banking.infra.service.invoice;

import com.banking.core_banking.domain.model.dto.account.request.WithdrawRequest;
import com.banking.core_banking.domain.model.dto.invoice.request.InvoicePaymentRequest;
import com.banking.core_banking.domain.model.dto.invoice.response.InvoiceDetailsResponse;
import com.banking.core_banking.domain.model.dto.invoice.response.InvoiceSummaryResponse;
import com.banking.core_banking.domain.model.entities.invoice.Invoice;
import com.banking.core_banking.domain.model.enums.card.InvoiceStatus;
import com.banking.core_banking.domain.repository.card.CardRepository;
import com.banking.core_banking.domain.repository.invoice.InvoiceRepository;
import com.banking.core_banking.domain.service.account.AccountService;
import com.banking.core_banking.domain.service.invoice.InvoiceService;
import com.banking.core_banking.exceptions.invoice.InvoiceAlreadyPaidException;
import com.banking.core_banking.exceptions.others.BusinessException;
import com.banking.core_banking.exceptions.others.ResourceNotFoundException;
import com.banking.core_banking.infra.mapper.invoice.InvoiceMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final CardRepository cardRepository;
    private final AccountService accountService;
    private final InvoiceMapper invoiceMapper;

    @Autowired
    public InvoiceServiceImpl(
            InvoiceRepository invoiceRepository,
            CardRepository cardRepository,
            AccountService accountService,
            InvoiceMapper invoiceMapper
    ) {
        this.invoiceRepository = invoiceRepository;
        this.cardRepository = cardRepository;
        this.accountService = accountService;
        this.invoiceMapper = invoiceMapper;
    }

    @Override
    public InvoiceDetailsResponse getInvoiceDetails(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + invoiceId));

        return invoiceMapper.toDetailsDto(invoice);
    }

    @Override
    public Page<InvoiceSummaryResponse> findInvoicesByCardId(Long cardId, Pageable pageable) {
        if (!cardRepository.existsById(cardId)) {
            throw new ResourceNotFoundException("Card not found with id: " + cardId);
        }

        Page<Invoice> invoicePage = invoiceRepository.findByCreditFunctionCardIdOrderByReferenceMonthDesc(cardId, pageable);

        return invoicePage.map(invoiceMapper::toSummaryDto);
    }

    @Override
    @Transactional
    public void payInvoice(Long invoiceId, InvoicePaymentRequest request) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new InvoiceAlreadyPaidException("Invoice " + invoiceId + " has already been paid.");
        }
        // TODO: Adicionar lógica para pagamentos parciais e valor > devido
        if (request.paymentAmount().compareTo(invoice.getTotalAmount()) != 0) {
            throw new BusinessException("Partial payments are not yet supported.");
        }

        Long accountIdToDebit = invoice.getCreditFunction().getCard().getAccount().getId();
        accountService.withdraw(accountIdToDebit, new WithdrawRequest(request.paymentAmount()));

        invoice.markAsPaid();
        invoice.getCreditFunction().applyPayment(request.paymentAmount());
    }
}
