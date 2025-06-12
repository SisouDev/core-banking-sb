CREATE TABLE banking_products (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  product_type VARCHAR(31) NOT NULL,
                                  name VARCHAR(255) NOT NULL UNIQUE,
                                  description TEXT NOT NULL,
                                  active BOOLEAN NOT NULL DEFAULT true,


                                  min_amount DECIMAL(19, 2),
                                  max_amount DECIMAL(19, 2),
                                  default_interest_rate DECIMAL(10, 4),
                                  max_installments INTEGER
);