package com.banking.core_banking.domain.repository.product;

import com.banking.core_banking.domain.model.entities.product.BankingProduct;
import com.banking.core_banking.domain.model.enums.product.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

@Repository
public interface BankingProductRepository extends JpaRepository<BankingProduct, Long> {
    Page<BankingProduct> findAll(Pageable pageable);

    @Query("SELECT p FROM BankingProduct p WHERE TYPE(p) = LoanProduct AND p.active = true")
    Page<BankingProduct> findAllActiveLoanProducts(Pageable pageable);
}
