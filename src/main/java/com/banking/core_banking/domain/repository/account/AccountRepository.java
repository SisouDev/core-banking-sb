package com.banking.core_banking.domain.repository.account;

import com.banking.core_banking.domain.model.entities.account.Account;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByNumber(String accountNumber);

    Page<Account> findByCustomerId(Long customerId, Pageable pageable);

    Optional<Account> findByNumber(@NotBlank String s);
}
