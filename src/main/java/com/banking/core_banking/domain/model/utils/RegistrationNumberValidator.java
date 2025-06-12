package com.banking.core_banking.domain.model.utils;

import com.banking.core_banking.domain.model.enums.user.CustomerType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RegistrationNumberValidator implements ConstraintValidator<ValidRegistrationNumber, String> {

    private CustomerType type;

    @Override
    public void initialize(ValidRegistrationNumber constraintAnnotation) {
        this.type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(String registrationNumber, ConstraintValidatorContext context) {
        if (registrationNumber == null || registrationNumber.isBlank()) {
            return false;
        }

        return switch (type) {
            case PERSONAL ->
                    registrationNumber.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d\\-]{8,20}$");
            case BUSINESS ->
                    registrationNumber.matches("^\\d{8,20}$");
            default -> false;
        };
    }
}
