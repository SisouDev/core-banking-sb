package com.banking.core_banking.domain.repository.product;

import com.banking.core_banking.domain.model.entities.product.BankingProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

@Repository
public interface BankingProductRepository extends JpaRepository<BankingProduct, Long> {
    Page<BankingProduct> findAll(Pageable pageable);
}
