CREATE TABLE accounts (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          agency VARCHAR(4) NOT NULL,
                          `number` VARCHAR(10) NOT NULL UNIQUE,
                          balance DECIMAL(19, 2) NOT NULL,
                          account_type ENUM('CHECKING_ACCOUNT', 'SAVINGS_ACCOUNT') NOT NULL,
                          account_status ENUM('ACTIVE', 'BLOCKED', 'CLOSED') NOT NULL,
                          customer_id BIGINT NOT NULL,
                          manager_id BIGINT,
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP NOT NULL,
                          CONSTRAINT fk_account_customer FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE RESTRICT,
                          CONSTRAINT fk_account_manager FOREIGN KEY (manager_id) REFERENCES account_managers(id) ON DELETE SET NULL
);