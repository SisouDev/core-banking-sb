package com.banking.core_banking.domain.repository.product;

import com.banking.core_banking.domain.model.entities.product.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    Page<Loan> findByCustomerId(Long customerId, Pageable pageable);
}
