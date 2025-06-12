package com.banking.core_banking.domain.controller.product;

import com.banking.core_banking.domain.model.dto.product.request.LoanProductCreateRequest;
import com.banking.core_banking.domain.model.dto.product.request.LoanProductUpdateRequest;
import com.banking.core_banking.domain.model.dto.product.response.BankingProductResponse;
import com.banking.core_banking.domain.service.product.ProductAdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
//@PreAuthorize("hasRole('ADMIN')")
public class ProductAdminController {
    private final ProductAdminService productAdminService;

    public ProductAdminController(ProductAdminService productAdminService) {
        this.productAdminService = productAdminService;
    }

    @PostMapping("/loan")
    public ResponseEntity<BankingProductResponse> createLoanProduct(
            @Valid @RequestBody LoanProductCreateRequest request
    ) {
        BankingProductResponse response = productAdminService.createLoanProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/loan/{productId}")
    public ResponseEntity<BankingProductResponse> updateLoanProduct(
            @PathVariable Long productId,
            @Valid @RequestBody LoanProductUpdateRequest request
    ) {
        BankingProductResponse response = productAdminService.updateLoanProduct(productId, request);
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<Page<BankingProductResponse>> getAllProducts(Pageable pageable) {
        Page<BankingProductResponse> response = productAdminService.getAllProducts(pageable);
        return ResponseEntity.ok(response);
    }
}
