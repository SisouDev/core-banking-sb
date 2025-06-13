package com.banking.core_banking.domain.model.entities.invoice;

import com.banking.core_banking.domain.model.entities.card.CreditFunction;
import com.banking.core_banking.domain.model.enums.card.InvoiceStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "invoices")
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    private LocalDate closingDate;

    @Column(nullable = false)
    private YearMonth referenceMonth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "credit_function_id", nullable = false)
    @ToString.Exclude
    private CreditFunction creditFunction;

    @OneToMany(
            mappedBy = "invoice",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @ToString.Exclude
    private List<InvoiceItem> items;

    public static Invoice create(CreditFunction creditFunction, YearMonth referenceMonth, LocalDate closingDate, LocalDate dueDate) {
        if (creditFunction == null || referenceMonth == null) {
            throw new IllegalArgumentException("Credit function and reference month are required.");
        }

        return new Invoice(
                null,
                BigDecimal.ZERO,
                dueDate,
                closingDate,
                referenceMonth,
                InvoiceStatus.OPEN,
                creditFunction,
                new ArrayList<>()
        );
    }

    public void addItem(String description, BigDecimal amount) {
        if (this.status != InvoiceStatus.OPEN) {
            throw new IllegalStateException("Cannot add items to an invoice that is not open.");
        }
        InvoiceItem newItem = InvoiceItem.create(this, description, amount);
        this.items.add(newItem);
        this.totalAmount = this.totalAmount.add(amount);
    }

    public void close() {
        if (this.status != InvoiceStatus.OPEN) {
            throw new IllegalStateException("Can only close an open invoice.");
        }
        this.status = InvoiceStatus.CLOSED;
    }

    public void markAsPaid() {
        this.status = InvoiceStatus.PAID;
    }

    public void markAsOverdue() {
        if (this.status != InvoiceStatus.CLOSED) {
            throw new IllegalStateException("Only a closed invoice can become overdue.");
        }
        this.status = InvoiceStatus.OVERDUE;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Invoice invoice = (Invoice) o;
        return getId() != null && Objects.equals(getId(), invoice.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

}
