package com.banking.core_banking.unit.service.product;
import com.banking.core_banking.domain.model.dto.product.request.LoanApplicationRequest;
import com.banking.core_banking.domain.model.dto.product.request.LoanInstallmentPaymentRequest;
import com.banking.core_banking.domain.model.entities.product.BankingProduct;
import com.banking.core_banking.domain.model.entities.product.Loan;
import com.banking.core_banking.domain.model.entities.product.LoanInstallment;
import com.banking.core_banking.domain.model.entities.product.LoanProduct;
import com.banking.core_banking.domain.model.entities.user.Customer;
import com.banking.core_banking.domain.model.enums.product.InstallmentStatus;
import com.banking.core_banking.domain.repository.product.BankingProductRepository;
import com.banking.core_banking.domain.repository.product.LoanInstallmentRepository;
import com.banking.core_banking.domain.repository.product.LoanRepository;
import com.banking.core_banking.domain.repository.user.CustomerRepository;
import com.banking.core_banking.domain.service.account.AccountService;
import com.banking.core_banking.exceptions.others.BusinessException;
import com.banking.core_banking.infra.mapper.product.LoanMapper;
import com.banking.core_banking.infra.service.product.LoanServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    @Mock
    private LoanRepository loanRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private BankingProductRepository bankingProductRepository;
    @Mock
    private LoanInstallmentRepository loanInstallmentRepository;
    @Mock
    private AccountService accountService;
    @Mock
    private LoanMapper loanMapper;

    @InjectMocks
    private LoanServiceImpl loanService;

    @Nested
    @DisplayName("Testes para applyForLoan()")
    class ApplyForLoanTests {

        @Test
        void whenApplicationIsValid_shouldCreateLoan() {

            var request = new LoanApplicationRequest(Long.valueOf(1), Long.valueOf(1), new BigDecimal("10000"), 12);
            var mockCustomer = mock(Customer.class);
            var mockLoanProduct = mock(LoanProduct.class);

            when(mockLoanProduct.getMinAmount()).thenReturn(new BigDecimal("1000.00"));
            when(mockLoanProduct.getMaxAmount()).thenReturn(new BigDecimal("50000.00"));
            when(mockLoanProduct.getMaxInstallments()).thenReturn(24);
            when(mockLoanProduct.getDefaultInterestRate()).thenReturn(new BigDecimal("0.02"));

            when(customerRepository.findById(request.customerId())).thenReturn(Optional.of(mockCustomer));
            when(bankingProductRepository.findById(request.productId())).thenReturn(Optional.of(mockLoanProduct));
            when(loanRepository.save(any(Loan.class))).thenReturn(mock(Loan.class));

            loanService.applyForLoan(request);

            verify(loanRepository, times(1)).save(any(Loan.class));
            verify(loanMapper, times(1)).toDetailsDto(any(Loan.class));
        }

        @Test
        void whenProductIsNotALoanProduct_shouldThrowBusinessException() {
            var request = new LoanApplicationRequest(Long.valueOf(1), Long.valueOf(1), new BigDecimal("10000"), 12);
            var mockCustomer = mock(Customer.class);
            var mockProduct = mock(BankingProduct.class);

            when(customerRepository.findById(request.customerId())).thenReturn(Optional.of(mockCustomer));
            when(bankingProductRepository.findById(request.productId())).thenReturn(Optional.of(mockProduct));

            assertThrows(BusinessException.class, () -> {
                loanService.applyForLoan(request);
            });
        }
    }

    @Nested
    @DisplayName("Testes para payInstallment()")
    class PayInstallmentTests {

        @Test
        void whenInstallmentIsValid_shouldPaySuccessfully() {
            Long installmentId = Long.valueOf(42);
            var request = new LoanInstallmentPaymentRequest(Long.valueOf(1));
            var mockInstallment = mock(LoanInstallment.class);

            when(mockInstallment.getStatus()).thenReturn(InstallmentStatus.PENDING);
            when(loanInstallmentRepository.findById(installmentId)).thenReturn(Optional.of(mockInstallment));

            loanService.payInstallment(installmentId, request);

            verify(accountService, times(1)).withdraw(any(), any());
            verify(mockInstallment, times(1)).markAsPaid();
        }

        @Test
        void whenInstallmentIsAlreadyPaid_shouldThrowBusinessException() {
            Long installmentId = Long.valueOf(42);
            var request = new LoanInstallmentPaymentRequest(Long.valueOf(1));
            var mockInstallment = mock(LoanInstallment.class);

            when(mockInstallment.getStatus()).thenReturn(InstallmentStatus.PAID);
            when(loanInstallmentRepository.findById(installmentId)).thenReturn(Optional.of(mockInstallment));

            assertThrows(BusinessException.class, () -> {
                loanService.payInstallment(installmentId, request);
            });

            verify(accountService, never()).withdraw(any(), any());
        }
    }
}