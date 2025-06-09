CREATE TABLE cards (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       `number` VARCHAR(255) NOT NULL UNIQUE,
                       holder_name VARCHAR(255) NOT NULL,
                       expiration_date VARCHAR(7) NOT NULL,
                       status ENUM('ACTIVE', 'INACTIVE', 'BLOCKED', 'CANCELED', 'EXPIRED') NOT NULL,
                       account_id BIGINT NOT NULL,
                       CONSTRAINT fk_card_account FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
);