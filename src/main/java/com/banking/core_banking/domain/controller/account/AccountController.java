package com.banking.core_banking.domain.controller.account;

import com.banking.core_banking.domain.model.dto.account.request.AccountCreateRequest;
import com.banking.core_banking.domain.model.dto.account.response.AccountDetailsResponse;
import com.banking.core_banking.domain.service.account.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountDetailsResponse> createAccount(@Valid @RequestBody AccountCreateRequest request) {
        AccountDetailsResponse response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDetailsResponse> getAccountDetails(@PathVariable Long accountId) {
        AccountDetailsResponse response = accountService.getAccountDetails(accountId);
        return ResponseEntity.ok(response);
    }
}