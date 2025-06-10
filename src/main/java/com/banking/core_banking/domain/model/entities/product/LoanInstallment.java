package com.banking.core_banking.domain.model.entities.product;

import com.banking.core_banking.domain.model.enums.product.InstallmentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "loan_installments")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoanInstallment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer installmentNumber;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal principalAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal interestAmount;

    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InstallmentStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_id", nullable = false)
    @ToString.Exclude
    private Loan loan;

    static LoanInstallment create(Loan loan, Integer installmentNumber, BigDecimal totalAmount, BigDecimal principalAmount, BigDecimal interestAmount, LocalDate dueDate) {
        if (loan == null || installmentNumber <= 0 || totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invalid installment data provided.");
        }

        return new LoanInstallment(null, installmentNumber, totalAmount, principalAmount, interestAmount, dueDate, null, InstallmentStatus.PENDING, loan);
    }

    public void markAsPaid() {
        if (this.status == InstallmentStatus.PAID) {
            System.out.println("Warning: Attempted to pay an already paid installment.");
            return;
        }
        this.status = InstallmentStatus.PAID;
        this.paymentDate = LocalDateTime.now();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        LoanInstallment that = (LoanInstallment) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
