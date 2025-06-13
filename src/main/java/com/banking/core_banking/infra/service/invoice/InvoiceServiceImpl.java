package com.banking.core_banking.infra.service.invoice;

import com.banking.core_banking.domain.model.dto.account.request.WithdrawRequest;
import com.banking.core_banking.domain.model.dto.invoice.request.InvoicePaymentRequest;
import com.banking.core_banking.domain.model.dto.invoice.response.InvoiceDetailsResponse;
import com.banking.core_banking.domain.model.dto.invoice.response.InvoiceSummaryResponse;
import com.banking.core_banking.domain.model.entities.invoice.Invoice;
import com.banking.core_banking.domain.model.entities.user.Customer;
import com.banking.core_banking.domain.model.entities.user.User;
import com.banking.core_banking.domain.model.enums.card.InvoiceStatus;
import com.banking.core_banking.domain.repository.card.CardRepository;
import com.banking.core_banking.domain.repository.invoice.InvoiceRepository;
import com.banking.core_banking.domain.repository.user.CustomerRepository;
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

import java.util.Objects;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final CardRepository cardRepository;
    private final AccountService accountService;
    private final InvoiceMapper invoiceMapper;
    private final CustomerRepository customerRepository;

    @Autowired
    public InvoiceServiceImpl(
            InvoiceRepository invoiceRepository,
            CardRepository cardRepository,
            AccountService accountService,
            InvoiceMapper invoiceMapper,
            CustomerRepository customerRepository
    ) {
        this.invoiceRepository = invoiceRepository;
        this.cardRepository = cardRepository;
        this.accountService = accountService;
        this.invoiceMapper = invoiceMapper;
        this.customerRepository = customerRepository;
    }

    @Override
    public InvoiceDetailsResponse getInvoiceDetails(User loggedInUser, Long invoiceId) {
        Invoice invoice = findInvoiceAndValidateOwnership(loggedInUser, invoiceId);
        return invoiceMapper.toDetailsDto(invoice);
    }


    @Override
    public Page<InvoiceSummaryResponse> findInvoicesForUser(User loggedInUser, Pageable pageable) {
        Customer customer = customerRepository.findByUserId(loggedInUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found for the logged-in user."));

        Page<Invoice> invoicePage = invoiceRepository.findByCustomerId(customer.getId(), pageable);
        return invoicePage.map(invoiceMapper::toSummaryDto);
    }


    @Override
    @Transactional
    public void payInvoice(User loggedInUser, Long invoiceId, InvoicePaymentRequest request) {
        Invoice invoice = findInvoiceAndValidateOwnership(loggedInUser, invoiceId);

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new InvoiceAlreadyPaidException("Invoice " + invoiceId + " has already been paid.");
        }
        if (request.paymentAmount().compareTo(invoice.getTotalAmount()) != 0) {
            throw new BusinessException("Partial payments are not yet supported.");
        }

        Long accountIdToDebit = invoice.getCreditFunction().getCard().getAccount().getId();
        accountService.withdraw(accountIdToDebit, new WithdrawRequest(request.paymentAmount(), "Pagamento Fatura"));

        invoice.markAsPaid();
        invoice.getCreditFunction().applyPayment(request.paymentAmount());
    }

    private Invoice findInvoiceAndValidateOwnership(User loggedInUser, Long invoiceId) {
        Customer customer = customerRepository.findByUserId(loggedInUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found for the logged-in user."));

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + invoiceId));

        if (!Objects.equals(invoice.getCreditFunction().getCard().getAccount().getCustomer().getId(), customer.getId())) {
            throw new BusinessException("Invoice does not belong to the authenticated user.");
        }
        return invoice;
    }
}
