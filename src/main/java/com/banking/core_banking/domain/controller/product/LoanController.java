package com.banking.core_banking.domain.controller.product;

import com.banking.core_banking.domain.model.dto.product.request.LoanApplicationRequest;
import com.banking.core_banking.domain.model.dto.product.response.BankingProductResponse;
import com.banking.core_banking.domain.model.dto.product.response.LoanDetailsResponse;
import com.banking.core_banking.domain.model.entities.user.User;
import com.banking.core_banking.domain.service.product.LoanService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("/products")
    public ResponseEntity<Page<BankingProductResponse>> getAvailableLoanProducts(Pageable pageable) {
        Page<BankingProductResponse> response = loanService.getAvailableLoanProducts(pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<LoanDetailsResponse> applyForLoan(
            @AuthenticationPrincipal User loggedInUser,
            @Valid @RequestBody LoanApplicationRequest request
    ) {
        LoanDetailsResponse response = loanService.applyForLoan(loggedInUser, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{loanId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LoanDetailsResponse> approveLoan(@PathVariable Long loanId) {
        LoanDetailsResponse response = loanService.approveLoan(loanId);
        return ResponseEntity.ok(response);
    }
}
