package com.banking.core_banking.domain.controller.account;
import com.banking.core_banking.domain.model.dto.account.response.TransactionResponse;
import com.banking.core_banking.domain.model.dto.card.request.CreditPurchaseRequest;
import com.banking.core_banking.domain.model.dto.card.request.DebitPurchaseRequest;
import com.banking.core_banking.domain.model.entities.user.User;
import com.banking.core_banking.domain.service.account.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/purchase/debit")
    public ResponseEntity<TransactionResponse> performDebitPurchase(
            @AuthenticationPrincipal User loggedInUser,
            @Valid @RequestBody DebitPurchaseRequest request
    ) {
        TransactionResponse response = transactionService.performDebitPurchase(loggedInUser, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/purchase/credit")
    public ResponseEntity<Void> performCreditPurchase(
            @AuthenticationPrincipal User loggedInUser,
            @Valid @RequestBody CreditPurchaseRequest request
    ) {
        transactionService.performCreditPurchase(loggedInUser, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}