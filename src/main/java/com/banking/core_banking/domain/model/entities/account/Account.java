package com.banking.core_banking.domain.model.entities.account;

import com.banking.core_banking.domain.model.enums.account.AccountStatus;
import com.banking.core_banking.domain.model.enums.account.AccountType;
import com.banking.core_banking.domain.model.enums.account.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.banking.core_banking.domain.model.entities.user.Customer;
import com.banking.core_banking.domain.model.entities.user.AccountManager;

@Entity
@Table(name = "accounts")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 4)
    private String agency;

    @Column(nullable = false, unique = true, length = 10)
    private String number;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus accountStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @ToString.Exclude
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    @ToString.Exclude
    @Setter
    private AccountManager accountManager;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Transaction> transactions;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    public static Account create(Customer customer, AccountType accountType, String agency, String number, AccountManager manager) {
        if (customer == null || accountType == null || agency == null || agency.isBlank() || number == null || number.isBlank()) {
            throw new IllegalArgumentException("Customer, account type, agency, and number are required.");
        }

        return new Account(
                null,
                agency,
                number,
                BigDecimal.ZERO,
                accountType,
                AccountStatus.ACTIVE,
                customer,
                manager,
                new ArrayList<>(),
                null, null
        );
    }

    public void deposit(BigDecimal amount) {
        if (this.accountStatus != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Cannot deposit to an account that is not active.");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }

        this.balance = this.balance.add(amount);

        this.addTransaction(amount, TransactionType.DEPOSIT, "Deposit into account");
    }

    public void withdraw(BigDecimal amount) {
        if (this.accountStatus != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Cannot withdraw from an account that is not active.");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds for this withdrawal.");
        }

        this.balance = this.balance.subtract(amount);

        this.addTransaction(amount.negate(), TransactionType.WITHDRAWAL, "Withdrawal from account");
    }

    private void addTransaction(BigDecimal amount, TransactionType type, String description) {
        Transaction transaction = Transaction.create(this, amount, type, description);
        this.transactions.add(transaction);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Account account = (Account) o;
        return getId() != null && Objects.equals(getId(), account.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
