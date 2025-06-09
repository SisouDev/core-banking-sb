CREATE TABLE transactions (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              amount DECIMAL(19, 2) NOT NULL,
                              `type` ENUM('DEPOSIT', 'WITHDRAWAL', 'TRANSFER_SENT', 'TRANSFER_RECEIVED', 'DEBIT_CARD_PURCHASE', 'CREDIT_CARD_BILL_PAYMENT', 'LOAN_DISBURSEMENT', 'LOAN_INSTALLMENT_PAYMENT', 'INVESTMENT_PURCHASE', 'INVESTMENT_REDEMPTION', 'INSURANCE_PREMIUM_PAYMENT') NOT NULL,
                              description VARCHAR(255) NOT NULL,
                              `timestamp` TIMESTAMP NOT NULL,
                              account_id BIGINT NOT NULL,
                              CONSTRAINT fk_transaction_account FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE RESTRICT
);