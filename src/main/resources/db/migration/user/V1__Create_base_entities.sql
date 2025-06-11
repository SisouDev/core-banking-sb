CREATE TABLE employee_registration_sequence (
                                                id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE account_number_sequence (
                                         id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role ENUM('CUSTOMER', 'MANAGER', 'ADMIN', 'ACCOUNT_MANAGER', 'ANALYST') NOT NULL,
                       user_status ENUM('PENDING_VERIFICATION', 'ACTIVE', 'BLOCKED', 'INACTIVE') NOT NULL,
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP NOT NULL
);

CREATE TABLE employees (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           first_name VARCHAR(255) NOT NULL,
                           last_name VARCHAR(255) NOT NULL,
                           registration_code VARCHAR(255) NOT NULL UNIQUE,
                           hire_date TIMESTAMP NOT NULL,
                           user_id BIGINT NOT NULL UNIQUE,
                           CONSTRAINT fk_employee_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT
);

CREATE TABLE account_managers (
                                  id BIGINT PRIMARY KEY,
                                  CONSTRAINT fk_manager_employee FOREIGN KEY (id) REFERENCES employees(id) ON DELETE CASCADE
);

CREATE TABLE customers (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           customer_type VARCHAR(31) NOT NULL,
                           phone VARCHAR(20),
                           user_id BIGINT NOT NULL UNIQUE,
                           street VARCHAR(255) NOT NULL,
                           number VARCHAR(10) NOT NULL,
                           complement VARCHAR(255),
                           neighborhood VARCHAR(255) NOT NULL,
                           city VARCHAR(255) NOT NULL,
                           state VARCHAR(2) NOT NULL,
                           zip_code VARCHAR(9) NOT NULL,
                           country_code VARCHAR(2) NOT NULL,
                           name VARCHAR(255),
                           birth_date DATE,
                           company_name VARCHAR(255),
                           trade_name VARCHAR(255),
                           registration_number VARCHAR(255) UNIQUE,
                           CONSTRAINT fk_customer_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT
);