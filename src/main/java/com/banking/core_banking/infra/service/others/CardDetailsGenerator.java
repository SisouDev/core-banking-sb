package com.banking.core_banking.infra.service.others;

import com.banking.core_banking.domain.repository.card.CardRepository;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.Random;

@Component
public class CardDetailsGenerator {

    private final CardRepository cardRepository;

    public CardDetailsGenerator(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    /**
     * Gera um número de cartão de 16 dígitos único.
     */
    public String generateCardNumber() {
        Random random = new Random();
        String cardNumber;
        do {
            StringBuilder sb = new StringBuilder("4");
            for (int i = 0; i < 15; i++) {
                sb.append(random.nextInt(10));
            }
            cardNumber = sb.toString();
        } while (cardRepository.existsByNumber(cardNumber));
        return cardNumber;
    }

    public YearMonth generateExpirationDate() {
        return YearMonth.now().plusYears(5);
    }
}