package com.banking.core_banking.infra.service.product;

import com.banking.core_banking.domain.model.dto.account.request.DepositRequest;
import com.banking.core_banking.domain.model.dto.account.request.WithdrawRequest;
import com.banking.core_banking.domain.model.dto.product.request.LoanApplicationRequest;
import com.banking.core_banking.domain.model.dto.product.request.LoanInstallmentPaymentRequest;
import com.banking.core_banking.domain.model.dto.product.response.BankingProductResponse;
import com.banking.core_banking.domain.model.dto.product.response.LoanDetailsResponse;
import com.banking.core_banking.domain.model.dto.product.response.LoanSummaryResponse;
import com.banking.core_banking.domain.model.entities.account.Account;
import com.banking.core_banking.domain.model.entities.product.BankingProduct;
import com.banking.core_banking.domain.model.entities.product.Loan;
import com.banking.core_banking.domain.model.entities.product.LoanInstallment;
import com.banking.core_banking.domain.model.entities.product.LoanProduct;
import com.banking.core_banking.domain.model.entities.user.Customer;
import com.banking.core_banking.domain.model.entities.user.User;
import com.banking.core_banking.domain.model.enums.account.AccountStatus;
import com.banking.core_banking.domain.model.enums.account.AccountType;
import com.banking.core_banking.domain.model.enums.product.InstallmentStatus;
import com.banking.core_banking.domain.model.enums.product.ProductType;
import com.banking.core_banking.domain.model.utils.FinancialCalculator;
import com.banking.core_banking.domain.repository.account.AccountRepository;
import com.banking.core_banking.domain.repository.product.BankingProductRepository;
import com.banking.core_banking.domain.repository.product.LoanInstallmentRepository;
import com.banking.core_banking.domain.repository.product.LoanRepository;
import com.banking.core_banking.domain.repository.user.CustomerRepository;
import com.banking.core_banking.domain.service.account.AccountService;
import com.banking.core_banking.domain.service.product.LoanService;
import com.banking.core_banking.exceptions.others.BusinessException;
import com.banking.core_banking.exceptions.others.ResourceNotFoundException;
import com.banking.core_banking.infra.mapper.product.LoanMapper;
import com.banking.core_banking.infra.mapper.product.ProductMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;
    private final BankingProductRepository bankingProductRepository;
    private final LoanInstallmentRepository loanInstallmentRepository;
    private final AccountService accountService;
    private final LoanMapper loanMapper;
    private final ProductMapper productMapper;
    private final AccountRepository accountRepository;

    @Autowired
    public LoanServiceImpl(
            LoanRepository loanRepository,
            CustomerRepository customerRepository,
            BankingProductRepository bankingProductRepository,
            LoanInstallmentRepository loanInstallmentRepository,
            AccountService accountService,
            LoanMapper loanMapper, ProductMapper productMapper, AccountRepository accountRepository
    ) {
        this.loanRepository = loanRepository;
        this.customerRepository = customerRepository;
        this.bankingProductRepository = bankingProductRepository;
        this.loanInstallmentRepository = loanInstallmentRepository;
        this.accountService = accountService;
        this.loanMapper = loanMapper;
        this.productMapper = productMapper;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public LoanDetailsResponse approveLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + loanId));

        loan.approve();

        Customer customer = loan.getCustomer();
        Account customerAccount = accountRepository.findFirstByCustomerIdAndAccountTypeAndStatus(
                        customer.getId(),
                        AccountType.CHECKING_ACCOUNT,
                        AccountStatus.ACTIVE
                )
                .orElseThrow(() -> new BusinessException("No active checking account found for customer " + customer.getId() + " to disburse the loan."));

        accountService.deposit(customerAccount.getId(), new DepositRequest(loan.getPrincipalAmount()));

        List<LoanInstallment> installments = generateInstallments(loan);
        loanInstallmentRepository.saveAll(installments);
        loan.setLoanInstallments(installments);

        Loan savedLoan = loanRepository.save(loan);
        return loanMapper.toDetailsDto(savedLoan);
    }

    private List<LoanInstallment> generateInstallments(Loan loan) {
        List<LoanInstallment> installments = new ArrayList<>();
        BigDecimal principal = loan.getPrincipalAmount();
        BigDecimal monthlyRate = loan.getInterestRate();
        int numberOfInstallments = loan.getNumberOfInstallments();

        BigDecimal totalInstallmentAmount = FinancialCalculator.calculatePriceInstallment(
                principal, monthlyRate, numberOfInstallments
        );

        BigDecimal remainingPrincipal = principal;
        for (int i = 1; i <= numberOfInstallments; i++) {
            BigDecimal interestPortion = remainingPrincipal.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principalPortion = totalInstallmentAmount.subtract(interestPortion);

            if (i == numberOfInstallments) {
                principalPortion = remainingPrincipal;
                totalInstallmentAmount = remainingPrincipal.add(interestPortion);
            }

            LoanInstallment installment = LoanInstallment.create(loan, i, totalInstallmentAmount, principalPortion, interestPortion, LocalDate.now().plusMonths(i));
            installments.add(installment);
            remainingPrincipal = remainingPrincipal.subtract(principalPortion);
        }
        return installments;
    }


    @Override
    @Transactional
    public LoanDetailsResponse applyForLoan(User loggedInUser, LoanApplicationRequest request) {
        Customer customer = customerRepository.findByUserId(loggedInUser.getId())
                .orElseThrow();

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
    public void payInstallment(User loggedInUser, Long installmentId, LoanInstallmentPaymentRequest request) {
        Customer customer = customerRepository.findByUserId(loggedInUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found for the logged-in user."));

        LoanInstallment installment = loanInstallmentRepository.findById(installmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan installment not found with id: " + installmentId));

        if (!Objects.equals(installment.getLoan().getCustomer().getId(), customer.getId())) {
            throw new BusinessException("Installment does not belong to the authenticated user.");
        }

        Account sourceAccount = accountRepository.findById(request.sourceAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Source account not found with id: " + request.sourceAccountId()));

        if(!Objects.equals(sourceAccount.getCustomer().getId(), customer.getId())){
            throw new BusinessException("Source account does not belong to the authenticated user.");
        }

        accountService.withdraw(
                sourceAccount.getId(),
                new WithdrawRequest(installment.getTotalAmount(), "")
        );

        installment.markAsPaid();
        loanInstallmentRepository.save(installment);
    }

    @Override
    public Page<BankingProductResponse> getAvailableLoanProducts(Pageable pageable) {
        Page<BankingProduct> products = bankingProductRepository.findAllActiveLoanProducts(pageable);
        return products.map(productMapper::toDto);
    }
}
