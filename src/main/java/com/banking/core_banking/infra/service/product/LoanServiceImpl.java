package com.banking.core_banking.infra.service.product;

import com.banking.core_banking.domain.model.dto.account.request.WithdrawRequest;
import com.banking.core_banking.domain.model.dto.product.request.LoanApplicationRequest;
import com.banking.core_banking.domain.model.dto.product.request.LoanInstallmentPaymentRequest;
import com.banking.core_banking.domain.model.dto.product.response.LoanDetailsResponse;
import com.banking.core_banking.domain.model.dto.product.response.LoanSummaryResponse;
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
import com.banking.core_banking.domain.service.product.LoanService;
import com.banking.core_banking.exceptions.others.BusinessException;
import com.banking.core_banking.exceptions.others.ResourceNotFoundException;
import com.banking.core_banking.infra.mapper.product.LoanMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;
    private final BankingProductRepository bankingProductRepository;
    private final LoanInstallmentRepository loanInstallmentRepository;
    private final AccountService accountService;
    private final LoanMapper loanMapper;

    @Autowired
    public LoanServiceImpl(
            LoanRepository loanRepository,
            CustomerRepository customerRepository,
            BankingProductRepository bankingProductRepository,
            LoanInstallmentRepository loanInstallmentRepository,
            AccountService accountService,
            LoanMapper loanMapper
    ) {
        this.loanRepository = loanRepository;
        this.customerRepository = customerRepository;
        this.bankingProductRepository = bankingProductRepository;
        this.loanInstallmentRepository = loanInstallmentRepository;
        this.accountService = accountService;
        this.loanMapper = loanMapper;
    }

    @Override
    @Transactional
    public LoanDetailsResponse applyForLoan(LoanApplicationRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        BankingProduct product = bankingProductRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!(product instanceof LoanProduct loanProduct)) {
            throw new BusinessException("Product is not a valid loan product.");
        }

        Loan newLoan = Loan.create(
                customer,
                loanProduct,
                request.principalAmount(),
                request.numberOfInstallments()
        );

        Loan savedLoan = loanRepository.save(newLoan);

        return loanMapper.toDetailsDto(savedLoan);
    }

    @Override
    public LoanDetailsResponse getLoanDetails(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + loanId));
        return loanMapper.toDetailsDto(loan);
    }

    @Override
    public Page<LoanSummaryResponse> getLoansForCustomer(Long customerId, Pageable pageable) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found with id: " + customerId);
        }

        Page<Loan> loanPage = loanRepository.findByCustomerId(customerId, pageable);

        return loanPage.map(loanMapper::toSummaryDto);
    }

    @Override
    @Transactional
    public void payInstallment(Long installmentId, LoanInstallmentPaymentRequest request) {
        LoanInstallment installment = loanInstallmentRepository.findById(installmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan installment not found"));

        if (installment.getStatus() == InstallmentStatus.PAID) {
            throw new BusinessException("This installment has already been paid.");
        }

        accountService.withdraw(
                request.sourceAccountId(),
                new WithdrawRequest(installment.getTotalAmount())
        );

        installment.markAsPaid();
    }
}
