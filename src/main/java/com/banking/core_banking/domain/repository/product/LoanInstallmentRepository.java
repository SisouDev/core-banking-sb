package com.banking.core_banking.domain.repository.product;

import com.banking.core_banking.domain.model.entities.product.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Long> {
}
