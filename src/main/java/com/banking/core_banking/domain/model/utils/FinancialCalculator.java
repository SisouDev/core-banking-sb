package com.banking.core_banking.domain.model.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class FinancialCalculator {
    private static final int MONETARY_SCALE = 2;
    private static final int CALCULATION_SCALE = 16;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private FinancialCalculator() {
        throw new IllegalStateException("Utility class cannot be instantiated.");
    }

    public static BigDecimal calculatePriceInstallment(BigDecimal principal, BigDecimal monthlyRate, int numberOfInstallments) {
        if (principal == null || principal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Principal must be positive.");
        }
        if (monthlyRate == null || monthlyRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Monthly rate cannot be negative.");
        }
        if (numberOfInstallments <= 0) {
            throw new IllegalArgumentException("Number of installments must be positive.");
        }

        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(new BigDecimal(numberOfInstallments), MONETARY_SCALE, ROUNDING_MODE);
        }

        BigDecimal rateFactor = BigDecimal.ONE.add(monthlyRate);

        BigDecimal powerFactor = rateFactor.pow(numberOfInstallments, new MathContext(CALCULATION_SCALE));

        BigDecimal numerator = principal.multiply(monthlyRate).multiply(powerFactor);

        BigDecimal denominator = powerFactor.subtract(BigDecimal.ONE);

        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(new BigDecimal(numberOfInstallments), MONETARY_SCALE, ROUNDING_MODE);
        }

        return numerator.divide(denominator, MONETARY_SCALE, ROUNDING_MODE);
    }

    public static BigDecimal calculateSacFirstInstallment(BigDecimal principal, BigDecimal monthlyRate, int numberOfInstallments) {
        if (principal == null || principal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Principal must be positive.");
        }
        if (monthlyRate == null || monthlyRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Monthly rate cannot be negative.");
        }
        if (numberOfInstallments <= 0) {
            throw new IllegalArgumentException("Number of installments must be positive.");
        }

        BigDecimal amortization = principal.divide(new BigDecimal(numberOfInstallments), MONETARY_SCALE, ROUNDING_MODE);

        BigDecimal firstInterest = principal.multiply(monthlyRate).setScale(MONETARY_SCALE, ROUNDING_MODE);

        return amortization.add(firstInterest);
    }
}