package com.banking.core_banking.domain.model.entities.product;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("LOAN")
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class LoanProduct extends BankingProduct{
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal minAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal maxAmount;

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal defaultInterestRate;

    @Column(nullable = false)
    private Integer maxInstallments;

    private LoanProduct(String name, String description, BigDecimal minAmount, BigDecimal maxAmount, BigDecimal defaultInterestRate, Integer maxInstallments){
        super(null, name, description, true);
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.defaultInterestRate = defaultInterestRate;
        this.maxInstallments = maxInstallments;
    }

    public static LoanProduct create(String name, String description, BigDecimal minAmount, BigDecimal maxAmount, BigDecimal defaultInterestRate, Integer maxInstallments){
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("Loan product name cannot be blank.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Loan product description cannot be blank.");
        }
        if (minAmount == null || minAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Minimum amount must be a non-negative value.");
        }
        if (maxAmount == null || maxAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Maximum amount must be a positive value.");
        }
        if (maxAmount.compareTo(minAmount) < 0) {
            throw new IllegalArgumentException("Maximum amount cannot be less than the minimum amount.");
        }
        if (defaultInterestRate == null || defaultInterestRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Default interest rate must be a non-negative value.");
        }
        if (maxInstallments == null || maxInstallments <= 0) {
            throw new IllegalArgumentException("Maximum number of installments must be positive.");
        }
        return new LoanProduct(name, description, minAmount, maxAmount, defaultInterestRate, maxInstallments);
    }

    @Override
    public String getDisplayName() {
        return this.getName();
    }
}