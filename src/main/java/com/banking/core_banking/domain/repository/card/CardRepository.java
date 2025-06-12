package com.banking.core_banking.domain.repository.card;

import com.banking.core_banking.domain.model.entities.card.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    boolean existsByNumber(String cardNumber);

    @Query("SELECT c FROM Card c WHERE c.account.customer.id = :customerId")
    Page<Card> findByCustomerId(@Param("customerId") Long customerId, Pageable pageable);
}
