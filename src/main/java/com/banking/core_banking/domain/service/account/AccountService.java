package com.banking.core_banking.domain.service.account;

import com.banking.core_banking.domain.model.dto.account.request.AccountCreateRequest;
import com.banking.core_banking.domain.model.dto.account.request.DepositRequest;
import com.banking.core_banking.domain.model.dto.account.request.TransferRequest;
import com.banking.core_banking.domain.model.dto.account.request.WithdrawRequest;
import com.banking.core_banking.domain.model.dto.account.response.AccountDetailsResponse;
import com.banking.core_banking.domain.model.dto.account.response.AccountSummaryResponse;
import com.banking.core_banking.domain.model.dto.account.response.StatementLineResponse;
import com.banking.core_banking.domain.model.dto.account.response.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountService {
    AccountDetailsResponse createAccount(AccountCreateRequest request);

    AccountDetailsResponse getAccountDetails(Long accountId);

    Page<AccountSummaryResponse> getAccountsByCustomerId(Long customerId, Pageable pageable);
    Page<StatementLineResponse> getAccountStatement(Long accountId, Pageable pageable);

    TransactionResponse deposit(Long accountId, DepositRequest request);

    TransactionResponse withdraw(Long accountId, WithdrawRequest request);

    void transfer(Long sourceAccountId, TransferRequest request);
}
