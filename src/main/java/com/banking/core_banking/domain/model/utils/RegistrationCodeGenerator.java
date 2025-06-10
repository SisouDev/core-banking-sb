package com.banking.core_banking.domain.model.utils;

import com.banking.core_banking.domain.model.enums.user.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
public class RegistrationCodeGenerator {

    private final JdbcTemplate jdbcTemplate;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");

    public RegistrationCodeGenerator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String generateFor(Role role) {
        if (role == null || role.getAbbreviation() == null) {
            throw new IllegalArgumentException("Role and its abbreviation cannot be null.");
        }

        String rolePrefix = role.getAbbreviation();
        String datePart = LocalDate.now().format(DATE_FORMATTER);

        Long sequenceValue = getNextSequenceValue();

        String sequentialPart = String.format("%04d", sequenceValue);

        return rolePrefix + datePart + sequentialPart;
    }

    private Long getNextSequenceValue() {
        final String sql = "INSERT INTO employee_registration_sequence () VALUES ()";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            return ps;
        }, keyHolder);

        Long newId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        if (newId == null) {
            throw new IllegalStateException("Could not retrieve a value from the employee registration sequence table.");
        }
        return newId;
    }
}