package com.banking.core_banking.domain.repository.card;

import com.banking.core_banking.domain.model.entities.card.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    boolean existsByNumber(String cardNumber);
}
