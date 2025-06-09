CREATE TABLE card_debit_functions (
                                      card_id BIGINT PRIMARY KEY,
                                      daily_withdrawal_limit DECIMAL(19, 2) NOT NULL,
                                      daily_transaction_limit DECIMAL(19, 2) NOT NULL,
                                      CONSTRAINT fk_debit_function_card FOREIGN KEY (card_id) REFERENCES cards(id) ON DELETE CASCADE
);