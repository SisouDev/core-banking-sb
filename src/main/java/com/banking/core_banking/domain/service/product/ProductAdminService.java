package com.banking.core_banking.domain.service.product;

import com.banking.core_banking.domain.model.dto.product.request.LoanProductCreateRequest;
import com.banking.core_banking.domain.model.dto.product.request.LoanProductUpdateRequest;
import com.banking.core_banking.domain.model.dto.product.response.BankingProductResponse;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

public interface ProductAdminService {
    BankingProductResponse createLoanProduct(LoanProductCreateRequest request);
    BankingProductResponse updateLoanProduct(Long productId, LoanProductUpdateRequest request);
    Page<BankingProductResponse> getAllProducts(Pageable pageable);
}
