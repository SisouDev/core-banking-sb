CREATE TABLE card_credit_functions (
                                       card_id BIGINT PRIMARY KEY,
                                       credit_limit DECIMAL(19, 2) NOT NULL,
                                       available_limit DECIMAL(19, 2) NOT NULL,
                                       invoice_closing_day INTEGER NOT NULL,
                                       CONSTRAINT fk_credit_function_card FOREIGN KEY (card_id) REFERENCES cards(id) ON DELETE CASCADE
);