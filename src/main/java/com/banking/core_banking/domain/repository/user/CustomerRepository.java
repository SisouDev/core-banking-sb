package com.banking.core_banking.domain.repository.user;

import com.banking.core_banking.domain.model.entities.user.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
