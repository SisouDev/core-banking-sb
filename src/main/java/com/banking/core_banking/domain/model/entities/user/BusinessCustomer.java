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

@Entity
@DiscriminatorValue("BUSINESS")
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class BusinessCustomer extends Customer{
    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String tradeName;

    @Column(unique = true, nullable = false)
    @ValidRegistrationNumber(type = CustomerType.BUSINESS)
    private String registrationNumber ;

    private BusinessCustomer(User user, Address address, String phone, String companyName, String registrationNumber, String tradeName){
        super(null, phone, address, user);
        this.companyName = companyName;
        this.tradeName = tradeName;
        this.registrationNumber = registrationNumber;
    }

    public static BusinessCustomer create(User user, Address address, String phone, String companyName, String registrationNumber, String tradeName){
        if (companyName == null || companyName.isBlank()) {
            throw new IllegalArgumentException("Company name cannot be blank.");
        }
        if (registrationNumber == null || registrationNumber.isBlank()) {
            throw new IllegalArgumentException("Registration number (CNPJ) cannot be blank.");
        }
        return new BusinessCustomer(user, address, phone, companyName, registrationNumber, tradeName);
    }

    @Override
    public String getDisplayName() {
        return this.companyName;
    }
}