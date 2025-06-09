CREATE TABLE invoice_items (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               description VARCHAR(255) NOT NULL,
                               amount DECIMAL(19, 2) NOT NULL,
                               purchase_date TIMESTAMP NOT NULL,
                               invoice_id BIGINT NOT NULL,
                               CONSTRAINT fk_item_invoice FOREIGN KEY (invoice_id) REFERENCES invoices(id) ON DELETE CASCADE
);