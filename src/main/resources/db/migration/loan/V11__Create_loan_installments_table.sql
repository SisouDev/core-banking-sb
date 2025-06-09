CREATE TABLE loan_installments (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   installment_number INTEGER NOT NULL,
                                   total_amount DECIMAL(19, 2) NOT NULL,
                                   principal_amount DECIMAL(19, 2) NOT NULL,
                                   interest_amount DECIMAL(19, 2) NOT NULL,
                                   due_date DATE NOT NULL,
                                   payment_date TIMESTAMP,
                                   status ENUM('PENDING', 'PAID', 'OVERDUE') NOT NULL,
                                   loan_id BIGINT NOT NULL,
                                   CONSTRAINT fk_installment_loan FOREIGN KEY (loan_id) REFERENCES loans(id) ON DELETE CASCADE,
                                   UNIQUE (loan_id, installment_number)
);