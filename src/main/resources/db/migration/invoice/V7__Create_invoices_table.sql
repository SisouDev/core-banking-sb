CREATE TABLE invoices (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          total_amount DECIMAL(19, 2) NOT NULL,
                          due_date DATE NOT NULL,
                          closing_date DATE NOT NULL,
                          reference_month VARCHAR(7) NOT NULL,
                          status ENUM('OPEN', 'CLOSED', 'PAID', 'OVERDUE') NOT NULL,
                          credit_function_id BIGINT NOT NULL,
                          CONSTRAINT fk_invoice_credit_function FOREIGN KEY (credit_function_id) REFERENCES card_credit_functions(card_id) ON DELETE RESTRICT
);