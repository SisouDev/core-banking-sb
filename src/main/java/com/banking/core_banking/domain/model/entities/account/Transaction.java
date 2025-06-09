package com.banking.core_banking.domain.model.entities.account;

import com.banking.core_banking.domain.model.enums.account.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    @ToString.Exclude
    private Account account;


    static Transaction create(Account account, BigDecimal amount, TransactionType type, String description) {
        if (account == null) {
            throw new IllegalArgumentException("Transaction must be associated with an account.");
        }
        if (amount == null) {
            throw new IllegalArgumentException("Transaction amount cannot be null.");
        }

        return new Transaction(null, amount, type, description, null, account);
    }

}