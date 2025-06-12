package com.banking.core_banking.domain.model.entities.product;

import com.banking.core_banking.domain.model.enums.product.LoanStatus;
import com.banking.core_banking.domain.model.utils.FinancialCalculator;
import com.banking.core_banking.exceptions.others.BusinessException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;
import com.banking.core_banking.domain.model.entities.user.Customer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "loans")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal principalAmount;

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal interestRate;

    @Column(nullable = false)
    private Integer numberOfInstallments;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status;

    @Column(nullable = false)
    private LocalDateTime disbursementDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    @ToString.Exclude
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @ToString.Exclude
    private LoanProduct loanProduct;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<LoanInstallment> loanInstallments;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public static Loan create(Customer customer, LoanProduct loanProduct, BigDecimal principalAmount, Integer numberOfInstallments){
        if (customer == null || loanProduct == null) {
            throw new IllegalArgumentException("Customer and Loan Product cannot be null.");
        }
        if (principalAmount.compareTo(loanProduct.getMinAmount()) < 0 || principalAmount.compareTo(loanProduct.getMaxAmount()) > 0) {
            throw new IllegalArgumentException("Principal amount is outside the allowed range for this product.");
        }
        if (numberOfInstallments > loanProduct.getMaxInstallments()) {
            throw new IllegalArgumentException("Number of installments exceeds the maximum allowed for this product.");
        }
        Loan newLoan = new Loan(
                null,
                principalAmount,
                loanProduct.getDefaultInterestRate(),
                numberOfInstallments,
                LoanStatus.ACTIVE,
                LocalDateTime.now(),
                customer,
                loanProduct,
                new ArrayList<>(),
                null, null
        );

        newLoan.generateInstallments();

        return newLoan;
    }

    public void approve() {
        if (this.status != LoanStatus.REQUESTED) {
            throw new BusinessException("Only loans with REQUESTED status can be approved.");
        }
        this.status = LoanStatus.APPROVED;
        this.disbursementDate = LocalDateTime.now();
    }

    private void generateInstallments() {
        BigDecimal installmentAmount = FinancialCalculator.calculatePriceInstallment(
                this.principalAmount,
                this.interestRate,
                this.numberOfInstallments
        );

        BigDecimal outstandingBalance = this.principalAmount;

        for (int i = 1; i <= this.numberOfInstallments; i++) {
            BigDecimal interestForMonth = outstandingBalance.multiply(this.interestRate)
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal principalForMonth = installmentAmount.subtract(interestForMonth);

            if (i == this.numberOfInstallments) {
                principalForMonth = outstandingBalance;
                installmentAmount = principalForMonth.add(interestForMonth);
            }

            outstandingBalance = outstandingBalance.subtract(principalForMonth);

            LocalDate dueDate = this.disbursementDate.toLocalDate().plusMonths(i);

            LoanInstallment installment = LoanInstallment.create(this, i, installmentAmount, principalForMonth, interestForMonth, dueDate);
            this.loanInstallments.add(installment);
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Loan that = (Loan) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

}
