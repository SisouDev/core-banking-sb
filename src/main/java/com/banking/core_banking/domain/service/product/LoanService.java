package com.banking.core_banking.domain.service.product;

import com.banking.core_banking.domain.model.dto.product.request.LoanApplicationRequest;
import com.banking.core_banking.domain.model.dto.product.request.LoanInstallmentPaymentRequest;
import com.banking.core_banking.domain.model.dto.product.response.BankingProductResponse;
import com.banking.core_banking.domain.model.dto.product.response.LoanDetailsResponse;
import com.banking.core_banking.domain.model.dto.product.response.LoanSummaryResponse;
import com.banking.core_banking.domain.model.entities.user.User;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

public interface LoanService {
    LoanDetailsResponse applyForLoan(User loggedInUser, LoanApplicationRequest request);
    LoanDetailsResponse getLoanDetails(Long loanId);
    Page<LoanSummaryResponse> getLoansForCustomer(Long customerId, Pageable pageable);
    void payInstallment(Long installmentId, LoanInstallmentPaymentRequest request);
    LoanDetailsResponse approveLoan(Long loanId);

    Page<BankingProductResponse> getAvailableLoanProducts(Pageable pageable);
}
