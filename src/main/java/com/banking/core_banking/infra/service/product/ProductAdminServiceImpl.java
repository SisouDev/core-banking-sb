package com.banking.core_banking.infra.service.product;

import com.banking.core_banking.domain.model.dto.product.request.LoanProductCreateRequest;
import com.banking.core_banking.domain.model.dto.product.request.LoanProductUpdateRequest;
import com.banking.core_banking.domain.model.dto.product.response.BankingProductResponse;
import com.banking.core_banking.domain.model.entities.product.BankingProduct;
import com.banking.core_banking.domain.model.entities.product.LoanProduct;
import com.banking.core_banking.domain.repository.product.BankingProductRepository;
import com.banking.core_banking.domain.service.product.ProductAdminService;
import com.banking.core_banking.exceptions.others.BusinessException;
import com.banking.core_banking.exceptions.others.ResourceNotFoundException;
import com.banking.core_banking.infra.mapper.product.ProductMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

@Service
public class ProductAdminServiceImpl implements ProductAdminService {
    private final ProductMapper productMapper;
    private final BankingProductRepository bankingProductRepository;

    @Autowired
    public ProductAdminServiceImpl(
            ProductMapper productMapper,
            BankingProductRepository bankingProductRepository
    ) {
        this.productMapper = productMapper;
        this.bankingProductRepository = bankingProductRepository;
    }

    @Override
    @Transactional
    public BankingProductResponse createLoanProduct(LoanProductCreateRequest request) {
        LoanProduct newProduct = productMapper.toEntity(request);
        LoanProduct savedProduct = bankingProductRepository.save(newProduct);
        return productMapper.toDto(savedProduct);
    }

    @Override
    @Transactional
    public BankingProductResponse updateLoanProduct(Long productId, LoanProductUpdateRequest request) {
        BankingProduct product = bankingProductRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if(!(product instanceof LoanProduct loanProduct)){
            throw new BusinessException("Product is not a Loan Product and cannot be updated with these details.");
        }

        productMapper.updateFromDto(request, loanProduct);
        BankingProduct updatedProduct = bankingProductRepository.save(loanProduct);

        return productMapper.toDto(updatedProduct);
    }

    @Override
    public Page<BankingProductResponse> getAllProducts(Pageable pageable) {
        Page<BankingProduct> productPage = bankingProductRepository.findAll(pageable);
        return productPage.map(productMapper::toDto);
    }
}
