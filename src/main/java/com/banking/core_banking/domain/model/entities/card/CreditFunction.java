package com.banking.core_banking.domain.model.entities.card;

import com.banking.core_banking.domain.model.entities.invoice.Invoice;
import com.banking.core_banking.exceptions.others.BusinessException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "card_credit_functions")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreditFunction {
    @Id
    private Long id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal creditLimit;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal availableLimit;

    @Column(nullable = false)
    private Integer invoiceClosingDay;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "card_id")
    @ToString.Exclude
    private Card card;

    @OneToMany(mappedBy = "creditFunction", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // <-- Ajuste: FetchType explícito
    @ToString.Exclude
    private List<Invoice> invoices;

    static CreditFunction create(Card card, BigDecimal creditLimit, Integer invoiceClosingDay) {
        if (card == null) {
            throw new IllegalArgumentException("Credit function must be associated with a card.");
        }
        if (creditLimit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Credit limit must be positive.");
        }
        if (invoiceClosingDay < 1 || invoiceClosingDay > 28) {
            throw new IllegalArgumentException("Invoice closing day must be between 1 and 28.");
        }

        return new CreditFunction(null, creditLimit, creditLimit, invoiceClosingDay, card, new ArrayList<>());
    }

    public void makePurchase(BigDecimal purchaseAmount) {
        if (purchaseAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Purchase amount must be positive.");
        }
        if (purchaseAmount.compareTo(this.availableLimit) > 0) {
            throw new IllegalStateException("Insufficient credit limit for this purchase.");
        }

        this.availableLimit = this.availableLimit.subtract(purchaseAmount);
    }

    public void applyPayment(BigDecimal paymentAmount) {
        if (paymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive.");
        }

        this.availableLimit = this.availableLimit.add(paymentAmount);

        if (this.availableLimit.compareTo(this.creditLimit) > 0) {
            this.availableLimit = this.creditLimit;
        }
    }

    public void validateLimit(BigDecimal amountToSpend) {
        if (this.availableLimit.compareTo(amountToSpend) < 0) {
            throw new BusinessException("Insufficient credit limit.");
        }
    }

    public void useCredit(BigDecimal amountToSpend) {
        this.availableLimit = this.availableLimit.subtract(amountToSpend);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        CreditFunction that = (CreditFunction) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

}
