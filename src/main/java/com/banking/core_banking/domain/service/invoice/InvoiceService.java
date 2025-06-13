package com.banking.core_banking.domain.service.invoice;

import com.banking.core_banking.domain.model.dto.invoice.request.InvoicePaymentRequest;
import com.banking.core_banking.domain.model.dto.invoice.response.InvoiceDetailsResponse;
import com.banking.core_banking.domain.model.dto.invoice.response.InvoiceSummaryResponse;
import com.banking.core_banking.domain.model.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InvoiceService {
    InvoiceDetailsResponse getInvoiceDetails(User loggedInUser, Long invoiceId);
    Page<InvoiceSummaryResponse> findInvoicesForUser(User loggedInUser, Pageable pageable);
    void payInvoice(User loggedInUser, Long invoiceId, InvoicePaymentRequest request);
}
