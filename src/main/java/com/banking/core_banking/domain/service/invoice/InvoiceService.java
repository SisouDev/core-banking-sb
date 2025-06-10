package com.banking.core_banking.domain.service.invoice;

import com.banking.core_banking.domain.model.dto.invoice.request.InvoicePaymentRequest;
import com.banking.core_banking.domain.model.dto.invoice.response.InvoiceDetailsResponse;
import com.banking.core_banking.domain.model.dto.invoice.response.InvoiceSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InvoiceService {
    InvoiceDetailsResponse getInvoiceDetails(Long invoiceId);
    Page<InvoiceSummaryResponse> findInvoicesByCardId(Long cardId, Pageable pageable);
    void payInvoice(Long invoiceId, InvoicePaymentRequest request);
}
