package com.banking.core_banking.domain.model.utils;

import com.banking.core_banking.domain.model.enums.user.CustomerType;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RegistrationNumberValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRegistrationNumber {
    String message() default "Invalid registration number format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    CustomerType type();
}
