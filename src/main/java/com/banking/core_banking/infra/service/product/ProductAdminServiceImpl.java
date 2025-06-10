package com.banking.core_banking.infra.service.product;

import com.banking.core_banking.domain.model.dto.product.request.LoanProductCreateRequest;
import com.banking.core_banking.domain.model.dto.product.request.LoanProductUpdateRequest;
import com.banking.core_banking.domain.model.dto.product.response.BankingProductResponse;
import com.banking.core_banking.domain.model.entities.product.LoanProduct;
import com.banking.core_banking.domain.repository.product.BankingProductRepository;
import com.banking.core_banking.domain.service.product.ProductAdminService;
import com.banking.core_banking.infra.mapper.product.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;

@Service
public class ProductAdminServiceImpl implements ProductAdminService {
    private final ProductMapper productMapper;
    private final BankingProductRepository bankingProductRepository;

    @Autowired
    public ProductAdminServiceImpl(ProductMapper productMapper, BankingProductRepository bankingProductRepository) {
        this.productMapper = productMapper;
        this.bankingProductRepository = bankingProductRepository;
    }

    @Override
    public BankingProductResponse createLoanProduct(LoanProductCreateRequest request) {
        LoanProduct newProduct = productMapper.toEntity(request);
        LoanProduct savedProduct = bankingProductRepository.save(newProduct);
        return productMapper.toDto(savedProduct);
    }

    @Override
    public BankingProductResponse updateLoanProduct(Long productId, LoanProductUpdateRequest request) {
        return null;
    }

    @Override
    public Page<BankingProductResponse> getAllProducts(Pageable pageable) {
        return null;
    }
}
