package com.banking.core_banking.domain.model.utils;

import java.time.YearMonth;
import java.util.concurrent.ThreadLocalRandom;


public final class CardNumberGenerator {

    private CardNumberGenerator() {}

    public static String generateCardNumber() {
        long first15 = ThreadLocalRandom.current().nextLong(1_000_000_000_000_00L, 10_000_000_000_000_00L);
        return "4" + first15;
    }

    public static YearMonth generateExpirationDate() {
        return YearMonth.now().plusYears(3);
    }

    public static String generateCVV() {
        int cvv = ThreadLocalRandom.current().nextInt(100, 1000);
        return String.format("%03d", cvv);
    }
}