package com.banking.core_banking.infra.service.account;

import com.banking.core_banking.domain.model.dto.account.request.AccountCreateRequest;
import com.banking.core_banking.domain.model.dto.account.request.DepositRequest;
import com.banking.core_banking.domain.model.dto.account.request.TransferRequest;
import com.banking.core_banking.domain.model.dto.account.request.WithdrawRequest;
import com.banking.core_banking.domain.model.dto.account.response.AccountDetailsResponse;
import com.banking.core_banking.domain.model.dto.account.response.AccountSummaryResponse;
import com.banking.core_banking.domain.model.dto.account.response.StatementLineResponse;
import com.banking.core_banking.domain.model.dto.account.response.TransactionResponse;
import com.banking.core_banking.domain.model.entities.account.Account;
import com.banking.core_banking.domain.model.entities.account.Transaction;
import com.banking.core_banking.domain.model.entities.user.Customer;
import com.banking.core_banking.domain.model.utils.AccountNumberGenerator;
import com.banking.core_banking.domain.repository.account.AccountRepository;
import com.banking.core_banking.domain.repository.account.TransactionRepository;
import com.banking.core_banking.domain.repository.user.CustomerRepository;
import com.banking.core_banking.domain.service.account.AccountService;
import com.banking.core_banking.exceptions.account.SelfTransferException;
import com.banking.core_banking.exceptions.others.BusinessException;
import com.banking.core_banking.exceptions.others.ResourceNotFoundException;
import com.banking.core_banking.infra.mapper.account.AccountMapper;
import com.banking.core_banking.infra.mapper.account.TransactionMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AccountMapper accountMapper;
    private final AccountNumberGenerator accountNumberGenerator;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Autowired
    public AccountServiceImpl(AccountMapper accountMapper, AccountRepository accountRepository, CustomerRepository customerRepository, AccountNumberGenerator accountNumberGenerator, TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.accountMapper = accountMapper;
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.accountNumberGenerator = accountNumberGenerator;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    @Transactional
    public AccountDetailsResponse createAccount(AccountCreateRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.customerId()));
        String agency = accountNumberGenerator.generateAgency();
        String accountNumber = accountNumberGenerator.generate();

        if (accountRepository.existsByNumber(accountNumber)) {
            throw new BusinessException("Generated account number already exists. Please try again.");
        }

        Account newAccount = Account.create(customer, request.accountType(), agency, accountNumber, null);
        Account savedAccount = accountRepository.save(newAccount);
        return accountMapper.toDetailsDto(savedAccount);
    }

    @Override
    public AccountDetailsResponse getAccountDetails(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        return accountMapper.toDetailsDto(account);
    }

    @Override
    @Transactional
    public Page<AccountSummaryResponse> getAccountsByCustomerId(Long customerId, Pageable pageable) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found with id: " + customerId);
        }

        Page<Account> accountPage = accountRepository.findByCustomerId(customerId, pageable);
        return accountPage.map(accountMapper::toSummaryDto);
    }

    @Override
    @Transactional
    public Page<StatementLineResponse> getAccountStatement(Long accountId, Pageable pageable) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        Page<Transaction> transactionPage = transactionRepository.findByAccountIdOrderByTimestampDesc(accountId, pageable);

        List<StatementLineResponse> statementLines = new ArrayList<>();
        BigDecimal currentRunningBalance = account.getBalance();

        for (Transaction tx : transactionPage.getContent()) {
            statementLines.add(new StatementLineResponse(
                    tx.getId(),
                    tx.getAmount(),
                    tx.getType().name(),
                    tx.getDescription(),
                    tx.getTimestamp(),
                    currentRunningBalance
            ));
            currentRunningBalance = currentRunningBalance.subtract(tx.getAmount());
        }

        Collections.reverse(statementLines);
        return new PageImpl<>(statementLines, pageable, transactionPage.getTotalElements());
    }

    @Override
    @Transactional
    public TransactionResponse deposit(Long accountId, DepositRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        account.deposit(request.amount());

        accountRepository.save(account);

        return transactionMapper.toDto(account.getLatestTransaction());
    }

    @Override
    @Transactional
    public TransactionResponse withdraw(Long accountId, WithdrawRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        account.withdraw(request.amount());

        accountRepository.save(account);

        return transactionMapper.toDto(account.getLatestTransaction());
    }

    @Override
    @Transactional
    public void transfer(Long sourceAccountId, TransferRequest request) {
        Account sourceAccount = accountRepository.findById(sourceAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Source account not found"));

        Account destinationAccount = accountRepository.findByNumber(request.destinationAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Destination account not found"));

        if (Objects.equals(sourceAccount.getId(), destinationAccount.getId())) {
            throw new SelfTransferException("Source and destination accounts cannot be the same.");
        }

        sourceAccount.withdraw(request.amount());
        destinationAccount.deposit(request.amount());
    }
}
