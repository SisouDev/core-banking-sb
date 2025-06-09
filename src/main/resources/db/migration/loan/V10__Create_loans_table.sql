CREATE TABLE loans (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       principal_amount DECIMAL(19, 2) NOT NULL,
                       interest_rate DECIMAL(10, 4) NOT NULL,
                       number_of_installments INTEGER NOT NULL,
                       status ENUM('ACTIVE', 'PAID_OFF', 'IN_DEFAULT', 'IN_ARREARS') NOT NULL,
                       disbursement_date TIMESTAMP NOT NULL,
                       customer_id BIGINT NOT NULL,
                       product_id BIGINT NOT NULL,
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP NOT NULL,
                       CONSTRAINT fk_loan_customer FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE RESTRICT,
                       CONSTRAINT fk_loan_product FOREIGN KEY (product_id) REFERENCES banking_products(id) ON DELETE RESTRICT
);