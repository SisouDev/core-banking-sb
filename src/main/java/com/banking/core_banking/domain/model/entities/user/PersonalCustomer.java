package com.banking.core_banking.domain.model.entities.user;

import com.banking.core_banking.domain.model.enums.user.CustomerType;
import com.banking.core_banking.domain.model.utils.Address;
import com.banking.core_banking.domain.model.utils.ValidRegistrationNumber;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("PERSONAL")
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class PersonalCustomer extends Customer{
    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    @ValidRegistrationNumber(type = CustomerType.PERSONAL)
    private String registrationNumber;

    @Column(nullable = false)
    private LocalDate birthDate;

    private PersonalCustomer(User user, Address address, String phone, String name, String registrationNumber, LocalDate birthDate) {
        super(null, phone, address, user);
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.birthDate = birthDate;
    }

    public static PersonalCustomer create(User user, Address address, String phone, String name, String registrationNumber, LocalDate birthDate) {
        LocalDate minBirthDate = LocalDate.of(1930, 1, 1);
        LocalDate maxBirthDate = LocalDate.now().minusYears(16);
        if (birthDate.isBefore(minBirthDate) || birthDate.isAfter(maxBirthDate)) {
            throw new IllegalArgumentException("Customer must be under 125 years old and at least 16 years old.");
        }
        return new PersonalCustomer(user, address, phone, name, registrationNumber, birthDate);
    }

    @Override
    public String getDisplayName() {
        return this.name;
    }

}