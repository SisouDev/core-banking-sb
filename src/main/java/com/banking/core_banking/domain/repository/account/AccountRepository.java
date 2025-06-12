package com.banking.core_banking.domain.repository.account;

import com.banking.core_banking.domain.model.entities.account.Account;
import com.banking.core_banking.domain.model.enums.account.AccountStatus;
import com.banking.core_banking.domain.model.enums.account.AccountType;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByNumber(String accountNumber);

    Page<Account> findByCustomerId(Long customerId, Pageable pageable);

    Optional<Account> findByNumber(@NotBlank String s);

    @Query("SELECT a FROM Account a WHERE a.customer.id = :customerId AND a.accountType = :type AND a.accountStatus = :status")
    Optional<Account> findFirstByCustomerIdAndAccountTypeAndStatus(
            @Param("customerId") Long customerId,
            @Param("type") AccountType type,
            @Param("status") AccountStatus status
    );
}
