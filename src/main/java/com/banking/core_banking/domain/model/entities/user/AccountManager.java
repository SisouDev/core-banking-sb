package com.banking.core_banking.domain.model.entities.user;

import com.banking.core_banking.domain.model.entities.account.Account;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "account_managers")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountManager {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    @ToString.Exclude
    private Employee employee;

    @OneToMany(mappedBy = "accountManager", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Account> managedAccounts = new ArrayList<>();

    public static AccountManager create(Employee employee){
        if (employee == null) {
            throw new IllegalArgumentException("An AccountManager must be associated with a non-null Employee.");
        }
        AccountManager manager = new AccountManager();
        manager.employee = employee;
        manager.managedAccounts = new ArrayList<>();
        return manager;
    }

    public void addManagedAccount(Account account) {
        if (account != null && !this.managedAccounts.contains(account)) {
            this.managedAccounts.add(account);
            account.setAccountManager(this);
        }
    }

    public void removeManagedAccount(Account account) {
        if (account != null && this.managedAccounts.contains(account)) {
            this.managedAccounts.remove(account);
            account.setAccountManager(null);
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        AccountManager that = (AccountManager) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}