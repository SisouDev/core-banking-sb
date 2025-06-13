package com.banking.core_banking.domain.controller.invoice;

import com.banking.core_banking.domain.model.dto.invoice.request.InvoicePaymentRequest;
import com.banking.core_banking.domain.model.dto.invoice.response.InvoiceDetailsResponse;
import com.banking.core_banking.domain.model.dto.invoice.response.InvoiceSummaryResponse;
import com.banking.core_banking.domain.model.entities.user.User;
import com.banking.core_banking.domain.service.invoice.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public ResponseEntity<Page<InvoiceSummaryResponse>> getMyInvoices(
            @AuthenticationPrincipal User loggedInUser,
            Pageable pageable
    ) {
        Page<InvoiceSummaryResponse> response = invoiceService.findInvoicesForUser(loggedInUser, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<InvoiceDetailsResponse> getInvoiceDetails(
            @AuthenticationPrincipal User loggedInUser,
            @PathVariable Long invoiceId
    ) {
        InvoiceDetailsResponse response = invoiceService.getInvoiceDetails(loggedInUser, invoiceId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{invoiceId}/pay")
    public ResponseEntity<Void> payInvoice(
            @AuthenticationPrincipal User loggedInUser,
            @PathVariable Long invoiceId,
            @Valid @RequestBody InvoicePaymentRequest request
    ) {
        invoiceService.payInvoice(loggedInUser, invoiceId, request);
        return ResponseEntity.noContent().build();
    }
}