package com.banking.core_banking.domain.repository.account;

import com.banking.core_banking.domain.model.entities.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
