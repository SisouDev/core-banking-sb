package com.banking.core_banking.domain.repository.account;

import com.banking.core_banking.domain.model.entities.account.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByAccountIdOrderByTimestampDesc(Long accountId, Pageable pageable);

    Page<Transaction> findByAccountId(Long accountId, Pageable pageable);
}