package com.banking.core_banking.domain.model.entities.card;

import com.banking.core_banking.domain.model.entities.account.Account;
import com.banking.core_banking.domain.model.enums.card.CardStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Objects;

@Entity
@Table(name = "cards")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String number;

    @Column(nullable = false)
    private String holderName;

    @Column(nullable = false)
    private YearMonth expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private CardStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    @ToString.Exclude
    private Account account;

    @OneToOne(mappedBy = "card", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private DebitFunction debitFunction;

    @OneToOne(mappedBy = "card", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private CreditFunction creditFunction;

    public static Card create(Account account, String holderName, String cardNumber, YearMonth expirationDate) {
        if (account == null || holderName == null || holderName.isBlank() || cardNumber == null || cardNumber.isBlank() || expirationDate == null) {
            throw new IllegalArgumentException("Account, holder name, card number, and expiration date are required.");
        }

        return new Card(
                null,
                cardNumber,
                holderName,
                expirationDate,
                CardStatus.INACTIVE,
                account,
                null,
                null
        );
    }

    public boolean isDebit() { return this.debitFunction != null; }
    public boolean isCredit() { return this.creditFunction != null; }

    public void activateDebitFunction(BigDecimal dailyWithdrawalLimit, BigDecimal dailyTransactionLimit) {
        if (this.isDebit()) {
            throw new IllegalStateException("Debit function is already active for this card.");
        }
        this.debitFunction = DebitFunction.create(this, dailyWithdrawalLimit, dailyTransactionLimit);
    }

    public void activateCreditFunction(BigDecimal creditLimit, Integer invoiceClosingDay) {
        if (this.isCredit()) {
            throw new IllegalStateException("Credit function is already active for this card.");
        }
        this.creditFunction = CreditFunction.create(this, creditLimit, invoiceClosingDay);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Card card = (Card) o;
        return getId() != null && Objects.equals(getId(), card.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

}
