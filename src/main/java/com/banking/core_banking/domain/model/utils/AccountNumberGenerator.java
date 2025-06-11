package com.banking.core_banking.domain.model.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AccountNumberGenerator {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AccountNumberGenerator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String generate() {
        Long sequenceValue = jdbcTemplate.queryForObject("SELECT NEXT VALUE FOR account_number_sequence", Long.class);
        if (sequenceValue == null) {
            throw new IllegalStateException("Could not retrieve a value from the account number sequence.");
        }

        String baseNumber = String.format("%08d", sequenceValue);

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