package com.banking.core_banking.domain.model.entities.card;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "card_debit_functions")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DebitFunction {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "card_id")
    @ToString.Exclude
    private Card card;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal dailyWithdrawalLimit;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal dailyTransactionLimit;

    static DebitFunction create(Card card, BigDecimal dailyWithdrawalLimit, BigDecimal dailyTransactionLimit) {
        if (card == null) {
            throw new IllegalArgumentException("Debit function must be associated with a card.");
        }
        if (dailyWithdrawalLimit.compareTo(BigDecimal.ZERO) < 0 || dailyTransactionLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Debit limits cannot be negative.");
        }

        return new DebitFunction(null, card, dailyWithdrawalLimit, dailyTransactionLimit);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        DebitFunction that = (DebitFunction) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
