package com.banking.core_banking.domain.model.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class AccountNumberGenerator {

    public AccountNumberGenerator() {
    }

    public String generate() {
        long baseValue = ThreadLocalRandom.current().nextLong(10_000_000L, 100_000_000L);

        String baseNumber = String.format("%08d", baseValue);

        int checkDigit = calculateCheckDigit(baseNumber);

        return baseNumber + "-" + checkDigit;
    }

    public String generateAgency() {
        return "0001";
    }

    private int calculateCheckDigit(String number) {
        int sum = 0;
        int weight = 2;
        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(String.valueOf(number.charAt(i)));
            sum += digit * weight;
            weight++;
            if (weight > 9) {
                weight = 2;
            }
        }
        int remainder = sum % 11;
        int checkDigit = 11 - remainder;

        return (checkDigit == 10 || checkDigit == 11) ? 0 : checkDigit;
    }
}