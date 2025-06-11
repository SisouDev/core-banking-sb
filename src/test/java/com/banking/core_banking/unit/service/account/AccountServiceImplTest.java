package com.banking.core_banking.unit.service.account;

import com.banking.core_banking.domain.model.dto.account.request.AccountCreateRequest;
import com.banking.core_banking.domain.model.dto.account.request.TransferRequest;
import com.banking.core_banking.domain.model.dto.account.request.WithdrawRequest;
import com.banking.core_banking.domain.model.dto.account.response.AccountDetailsResponse;
import com.banking.core_banking.domain.model.entities.account.Account;
import com.banking.core_banking.domain.model.entities.user.Customer;
import com.banking.core_banking.domain.model.enums.account.AccountType;
import com.banking.core_banking.domain.model.utils.AccountNumberGenerator;
import com.banking.core_banking.domain.repository.account.AccountRepository;
import com.banking.core_banking.domain.repository.account.TransactionRepository;
import com.banking.core_banking.domain.repository.user.CustomerRepository;
import com.banking.core_banking.exceptions.account.InsufficientFundsException;
import com.banking.core_banking.exceptions.account.SelfTransferException;
import com.banking.core_banking.exceptions.others.ResourceNotFoundException;
import com.banking.core_banking.infra.mapper.account.AccountMapper;
import com.banking.core_banking.infra.mapper.account.TransactionMapper;
import com.banking.core_banking.infra.service.account.AccountServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private AccountMapper accountMapper;
    @Mock
    private AccountNumberGenerator accountNumberGenerator;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Nested
    @DisplayName("Testes para createAccount()")
    class CreateAccountTests {
        @Test
        void whenCustomerExists_shouldCreateAccountSuccessfully() {
            var request = new AccountCreateRequest(Long.valueOf(1), AccountType.CHECKING_ACCOUNT, null);
            var mockCustomer = mock(Customer.class);
            var savedAccount = mock(Account.class); // O que o save() vai retornar

            when(customerRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(mockCustomer));

            when(accountNumberGenerator.generateAgency()).thenReturn("0001");
            when(accountNumberGenerator.generate()).thenReturn("99887-7");

            when(accountRepository.existsByNumber("99887-7")).thenReturn(Boolean.valueOf(false));

            when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);
            when(accountMapper.toDetailsDto(savedAccount)).thenReturn(mock(AccountDetailsResponse.class));

            accountService.createAccount(request);

            verify(accountRepository, times(1)).save(any(Account.class));
        }

        @Test
        void whenCustomerNotFound_shouldThrowResourceNotFoundException() {
            var request = new AccountCreateRequest(Long.valueOf(1), AccountType.CHECKING_ACCOUNT, null);
            when(customerRepository.findById(Long.valueOf(1))).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> {
                accountService.createAccount(request);
            });
        }
    }

    @Nested
    @DisplayName("Testes para withdraw()")
    class WithdrawTests {

        @Test
        void whenWithdrawIsValid_shouldSucceed() {
            var request = new WithdrawRequest(new BigDecimal("100.00"));
            var mockAccount = mock(Account.class);

            when(accountRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(mockAccount));

            accountService.withdraw(Long.valueOf(1), request);

            verify(mockAccount, times(1)).withdraw(new BigDecimal("100.00"));
            verify(accountRepository, times(1)).save(mockAccount);
        }

        @Test
        void whenAccountHasInsufficientFunds_shouldThrowException() {
            // Arrange
            var request = new WithdrawRequest(new BigDecimal("500.00"));
            var mockAccount = mock(Account.class);

            when(accountRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(mockAccount));
            doThrow(new InsufficientFundsException("..."))
                    .when(mockAccount).withdraw(new BigDecimal("500.00"));

            assertThrows(InsufficientFundsException.class, () -> {
                accountService.withdraw(Long.valueOf(1), request);
            });

            verify(accountRepository, never()).save(mockAccount);
        }
    }

    @Nested
    @DisplayName("Testes para transfer()")
    class TransferTests {

        @Test
        void whenTransferIsValid_shouldWithdrawFromSourceAndDepositToDestination() {
            var request = new TransferRequest("98765-4", new BigDecimal("100.00"), "Test");

            var sourceAccount = mock(Account.class);
            var destinationAccount = mock(Account.class);

            when(sourceAccount.getId()).thenReturn(Long.valueOf(1));
            when(destinationAccount.getId()).thenReturn(Long.valueOf(2));

            when(accountRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(sourceAccount));
            when(accountRepository.findByNumber("98765-4")).thenReturn(Optional.of(destinationAccount));

            accountService.transfer(Long.valueOf(1), request);

            verify(sourceAccount).withdraw(new BigDecimal("100.00"));
            verify(destinationAccount).deposit(new BigDecimal("100.00"));
        }

        @Test
        void whenTransferringToSameAccount_shouldThrowSelfTransferException() {
            var request = new TransferRequest("12345-6", new BigDecimal("100.00"), "Test");
            var account = mock(Account.class);
            when(account.getId()).thenReturn(Long.valueOf(1));

            when(accountRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(account));
            when(accountRepository.findByNumber("12345-6")).thenReturn(Optional.of(account));

            // Act & Assert
            assertThrows(SelfTransferException.class, () -> {
                accountService.transfer(Long.valueOf(1), request);
            });
        }
    }
}
