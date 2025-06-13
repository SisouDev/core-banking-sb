-- #####################################################################
-- # SCRIPT DE SEED DEFINITIVO E CONSOLIDADO (V13 CORRIGIDO)
-- #####################################################################

-- Senha para 'password123': $2a$10$fP2s8dF9L5v3r9Y/kX6B7.sB/3f4E5gH6iJ7kL8mN9oP0qR1s2t3u
SET @hashed_password = '$2a$10$fP2s8dF9L5v3r9Y/kX6B7.sB/3f4E5gH6iJ7kL8mN9oP0qR1s2t3u';

-- =====================================================================
-- == FUNCIONÁRIOS E GERENTES
-- =====================================================================
INSERT INTO users (email, password, role, user_status, created_at, updated_at) VALUES ('fernando.lima@bank.com', @hashed_password, 'ACCOUNT_MANAGER', 'ACTIVE', NOW(), NOW());
SET @user_id_fernando = LAST_INSERT_ID();
INSERT INTO employees (user_id, first_name, last_name, registration_code, hire_date) VALUES (@user_id_fernando, 'Fernando', 'Lima', 'FUNC-MNG-001', NOW());
SET @employee_id_fernando = LAST_INSERT_ID();
INSERT INTO account_managers (id) VALUES (@employee_id_fernando);

INSERT INTO users (email, password, role, user_status, created_at, updated_at) VALUES ('gabriela.alves@bank.com', @hashed_password, 'ANALYST', 'ACTIVE', NOW(), NOW());
SET @user_id_gabriela = LAST_INSERT_ID();
INSERT INTO employees (user_id, first_name, last_name, registration_code, hire_date) VALUES (@user_id_gabriela, 'Gabriela', 'Alves', 'FUNC-ANL-002', NOW());

-- =====================================================================
-- == PRODUTOS BANCÁRIOS
-- =====================================================================
INSERT INTO banking_products (product_type, name, description, active, min_amount, max_amount, default_interest_rate, max_installments) VALUES ('LOAN', 'Crédito Pessoal', 'Dinheiro na mão para suas emergências.', true, 500.00, 20000.00, 0.04, 36);
SET @loan_product_1 = LAST_INSERT_ID();
INSERT INTO banking_products (product_type, name, description, active, min_amount, max_amount, default_interest_rate, max_installments) VALUES ('LOAN', 'Financiamento de Veículo', 'Conquiste seu carro novo.', true, 10000.00, 150000.00, 0.025, 60);
SET @loan_product_2 = LAST_INSERT_ID();

-- =====================================================================
-- == CLIENTE 1: ANA SILVA (Cenário Completo com Fatura)
-- =====================================================================
INSERT INTO users (email, password, role, user_status, created_at, updated_at) VALUES ('ana.silva@email.com', @hashed_password, 'CUSTOMER', 'ACTIVE', NOW(), NOW());
SET @user_id_ana = LAST_INSERT_ID();
INSERT INTO customers (customer_type, user_id, phone, street, number, complement, neighborhood, city, state, zip_code, country_code, name, registration_number, birth_date) VALUES ('PERSONAL', @user_id_ana, '21999990001', 'Rua das Flores', '10', 'Apto 101', 'Copacabana', 'Rio de Janeiro', 'RJ', '22010001', 'BR', 'Ana Silva', '11111111111', '1990-01-01');
SET @customer_id_ana = LAST_INSERT_ID();

INSERT INTO accounts (customer_id, agency, `number`, balance, account_type, account_status, created_at, updated_at, manager_id) VALUES (@customer_id_ana, '0001', '20001-1', 1500.75, 'CHECKING_ACCOUNT', 'ACTIVE', NOW(), NOW(), @employee_id_fernando);
SET @account_id_ana = LAST_INSERT_ID();

-- Cartão da Ana
INSERT INTO cards (account_id, `number`, holder_name, expiration_date, status) VALUES (@account_id_ana, '4901000011112222', 'ANA SILVA', '2028-06', 'ACTIVE');
SET @card_id_ana = LAST_INSERT_ID();
INSERT INTO card_debit_functions (card_id, daily_withdrawal_limit, daily_transaction_limit) VALUES (@card_id_ana, 1000.00, 5000.00);
INSERT INTO card_credit_functions (card_id, credit_limit, available_limit, invoice_closing_day) VALUES (@card_id_ana, 8000.00, 7349.25, 15); -- Limite disponível já ajustado
SET @credit_function_id_ana = @card_id_ana;

-- Fatura da Ana
INSERT INTO invoices (credit_function_id, total_amount, due_date, closing_date, reference_month, status)
VALUES (@credit_function_id_ana, 650.75, '2025-07-25', '2025-07-15', '2025-07', 'OPEN');
SET @invoice_id_ana = LAST_INSERT_ID();

-- Itens da Fatura da Ana
INSERT INTO invoice_items (invoice_id, description, amount, purchase_date)
VALUES (@invoice_id_ana, 'Compra na Lanchonete do Zé', 50.75, NOW() - INTERVAL 5 DAY);
INSERT INTO invoice_items (invoice_id, description, amount, purchase_date)
VALUES (@invoice_id_ana, 'Assinatura NetPrime', 59.90, NOW() - INTERVAL 3 DAY);
INSERT INTO invoice_items (invoice_id, description, amount, purchase_date)
VALUES (@invoice_id_ana, 'Posto de Gasolina Shell', 140.10, NOW() - INTERVAL 2 DAY);

-- Empréstimo da Ana
INSERT INTO loans (customer_id, product_id, principal_amount, interest_rate, number_of_installments, status, disbursement_date, created_at, updated_at) VALUES (@customer_id_ana, @loan_product_1, 5000.00, 0.04, 12, 'ACTIVE', NOW(), NOW(), NOW());

-- =====================================================================
-- == CLIENTE 2: BRUNO COSTA (Cenário com cartão bloqueado)
-- =====================================================================
INSERT INTO users (email, password, role, user_status, created_at, updated_at) VALUES ('bruno.costa@email.com', @hashed_password, 'CUSTOMER', 'ACTIVE', NOW(), NOW());
SET @user_id_bruno = LAST_INSERT_ID();
INSERT INTO customers (customer_type, user_id, phone, street, number, complement, neighborhood, city, state, zip_code, country_code, name, registration_number, birth_date) VALUES ('PERSONAL', @user_id_bruno, '11999990002', 'Avenida Paulista', '1500', 'Andar 10', 'Bela Vista', 'São Paulo', 'SP', '01310200', 'BR', 'Bruno Costa', '22222222222', '1985-03-12');
SET @customer_id_bruno = LAST_INSERT_ID();

INSERT INTO accounts (customer_id, agency, `number`, balance, account_type, account_status, created_at, updated_at, manager_id) VALUES (@customer_id_bruno, '0001', '20002-1', 500.00, 'CHECKING_ACCOUNT', 'ACTIVE', NOW(), NOW(), @employee_id_fernando);
SET @account_id_bruno = LAST_INSERT_ID();

-- Cartão do Bruno
INSERT INTO cards (account_id, `number`, holder_name, expiration_date, status) VALUES (@account_id_bruno, '4901333344445555', 'BRUNO COSTA', '2026-11', 'BLOCKED');
SET @card_id_bruno = LAST_INSERT_ID();
INSERT INTO card_debit_functions (card_id, daily_withdrawal_limit, daily_transaction_limit) VALUES (@card_id_bruno, 500.00, 1500.00);

-- =====================================================================
-- == CLIENTE 3: DIASTECH (Empresa)
-- =====================================================================
INSERT INTO users (email, password, role, user_status, created_at, updated_at) VALUES ('contato@diastech.com', @hashed_password, 'CUSTOMER', 'ACTIVE', NOW(), NOW());
SET @user_id_carla = LAST_INSERT_ID();
INSERT INTO customers (customer_type, user_id, phone, street, number, complement, neighborhood, city, state, zip_code, country_code, company_name, trade_name, registration_number) VALUES ('BUSINESS', @user_id_carla, '31999990003', 'Rua da Bahia', '200', 'Sala 50', 'Centro', 'Belo Horizonte', 'MG', '30160010', 'BR', 'Dias Tech Soluções', 'DiasTech', '33333333000133');
SET @customer_id_carla = LAST_INSERT_ID();
INSERT INTO accounts (customer_id, agency, `number`, balance, account_type, account_status, created_at, updated_at) VALUES (@customer_id_carla, '0002', '30001-1', 250000.00, 'CHECKING_ACCOUNT', 'ACTIVE', NOW(), NOW());

-- Adicionando um cartão de crédito corporativo inativo
SELECT id INTO @account_id_diastech FROM accounts WHERE `number` = '30001-1';

INSERT INTO cards (account_id, `number`, holder_name, expiration_date, status)
VALUES (@account_id_diastech, '5544444455556666', 'DIAS TECH SOLUCOES', '2029-01', 'INACTIVE');
SET @card_id_diastech = LAST_INSERT_ID();

-- Adicionando apenas a função de crédito (com limites zerados até a ativação)
INSERT INTO card_credit_functions (card_id, credit_limit, available_limit, invoice_closing_day)
VALUES (@card_id_diastech, 0, 0, 25);

-- Gerente de Contas 2: Sofia Ribeiro
INSERT INTO users (email, password, role, user_status, created_at, updated_at) VALUES ('sofia.ribeiro@bank.com', @hashed_password, 'ACCOUNT_MANAGER', 'ACTIVE', NOW(), NOW());
SET @user_id_sofia = LAST_INSERT_ID();
INSERT INTO employees (user_id, first_name, last_name, registration_code, hire_date) VALUES (@user_id_sofia, 'Sofia', 'Ribeiro', 'FUNC-MNG-003', NOW());
SET @employee_id_sofia = LAST_INSERT_ID();
INSERT INTO account_managers (id) VALUES (@employee_id_sofia);

-- =====================================================================
-- == MAIS PRODUTOS BANCÁRIOS
-- =====================================================================
INSERT INTO banking_products (product_type, name, description, active, min_amount, max_amount, default_interest_rate, max_installments) VALUES ('LOAN', 'Crédito Consignado', 'Taxas reduzidas para funcionários de empresas conveniadas.', true, 1000.00, 100000.00, 0.018, 72);
SET @loan_product_3 = LAST_INSERT_ID();

-- =====================================================================
-- == CLIENTE 4: DAVI MOREIRA (Duas contas, sem cartão)
-- =====================================================================
INSERT INTO users (email, password, role, user_status, created_at, updated_at) VALUES ('davi.moreira@email.com', @hashed_password, 'CUSTOMER', 'ACTIVE', NOW(), NOW());
SET @user_id_davi = LAST_INSERT_ID();
INSERT INTO customers (customer_type, user_id, phone, street, number, complement, neighborhood, city, state, zip_code, country_code, name, registration_number, birth_date) VALUES ('PERSONAL', @user_id_davi, '41999990004', 'Rua das Araucárias', '400', 'Casa', 'Santa Felicidade', 'Curitiba', 'PR', '82010000', 'BR', 'Davi Moreira', '44444444444', '1995-11-30');
SET @customer_id_davi = LAST_INSERT_ID();
INSERT INTO accounts (customer_id, agency, `number`, balance, account_type, account_status, created_at, updated_at, manager_id) VALUES (@customer_id_davi, '0001', '20004-1', 8500.00, 'CHECKING_ACCOUNT', 'ACTIVE', NOW(), NOW(), @employee_id_sofia);
INSERT INTO accounts (customer_id, agency, `number`, balance, account_type, account_status, created_at, updated_at, manager_id) VALUES (@customer_id_davi, '0001', '20004-2', 25000.00, 'SAVINGS_ACCOUNT', 'ACTIVE', NOW(), NOW(), @employee_id_sofia);

-- =====================================================================
-- == CLIENTE 5: EDUARDA FERNANDES (Usuário Pendente)
-- =====================================================================
INSERT INTO users (email, password, role, user_status, created_at, updated_at) VALUES ('eduarda.f@email.com', @hashed_password, 'CUSTOMER', 'PENDING_VERIFICATION', NOW(), NOW());
SET @user_id_eduarda = LAST_INSERT_ID();
INSERT INTO customers (customer_type, user_id, phone, street, number, complement, neighborhood, city, state, zip_code, country_code, name, registration_number, birth_date) VALUES ('PERSONAL', @user_id_eduarda, '51999990005', 'Av. Ipiranga', '500', '', 'Praia de Belas', 'Porto Alegre', 'RS', '90160090', 'BR', 'Eduarda Fernandes', '55555555555', '2001-02-18');

-- =====================================================================
-- == CLIENTE 6: FÁBIO GOMES (Cartão Inativo)
-- =====================================================================
INSERT INTO users (email, password, role, user_status, created_at, updated_at) VALUES ('fabio.gomes@email.com', @hashed_password, 'CUSTOMER', 'ACTIVE', NOW(), NOW());
SET @user_id_fabio = LAST_INSERT_ID();
INSERT INTO customers (customer_type, user_id, phone, street, number, complement, neighborhood, city, state, zip_code, country_code, name, registration_number, birth_date) VALUES ('PERSONAL', @user_id_fabio, '61999990006', 'Setor Hoteleiro Norte', '1', 'Bloco A', 'Asa Norte', 'Brasília', 'DF', '70710900', 'BR', 'Fábio Gomes', '66666666666', '1988-08-08');
SET @customer_id_fabio = LAST_INSERT_ID();
INSERT INTO accounts (customer_id, agency, `number`, balance, account_type, account_status, created_at, updated_at) VALUES (@customer_id_fabio, '0003', '40001-1', 1234.56, 'CHECKING_ACCOUNT', 'ACTIVE', NOW(), NOW());
SET @account_id_fabio = LAST_INSERT_ID();
INSERT INTO cards (account_id, `number`, holder_name, expiration_date, status) VALUES (@account_id_fabio, '4901999988887777', 'FABIO GOMES', '2029-01', 'INACTIVE');
SET @card_id_fabio = LAST_INSERT_ID();
INSERT INTO card_debit_functions (card_id, daily_withdrawal_limit, daily_transaction_limit) VALUES (@card_id_fabio, 0, 0);

-- =====================================================================
-- == CLIENTE 7: GLOBAL TRANSPORTES (Empresa com empréstimo)
-- =====================================================================
INSERT INTO users (email, password, role, user_status, created_at, updated_at) VALUES ('financeiro@globaltransportes.com', @hashed_password, 'CUSTOMER', 'ACTIVE', NOW(), NOW());
SET @user_id_global = LAST_INSERT_ID();
INSERT INTO customers (customer_type, user_id, phone, street, number, complement, neighborhood, city, state, zip_code, country_code, company_name, trade_name, registration_number) VALUES ('BUSINESS', @user_id_global, '71999990007', 'Av. Tancredo Neves', '3000', 'Torre Sul', 'Caminho das Árvores', 'Salvador', 'BA', '41820021', 'BR', 'Global Transportes Marítimos', 'GlobalTrans', '77777777000177');
SET @customer_id_global = LAST_INSERT_ID();
INSERT INTO accounts (customer_id, agency, `number`, balance, account_type, account_status, created_at, updated_at) VALUES (@customer_id_global, '0002', '30002-1', 543210.98, 'CHECKING_ACCOUNT', 'ACTIVE', NOW(), NOW());
INSERT INTO loans (customer_id, product_id, principal_amount, interest_rate, number_of_installments, status, disbursement_date, created_at, updated_at) VALUES (@customer_id_global, @loan_product_2, 100000.00, 0.025, 36, 'ACTIVE', NOW(), NOW(), NOW());




INSERT INTO users (email, password, role, user_status, created_at, updated_at)
VALUES ('livia.ramos@email.com', @hashed_password, 'CUSTOMER', 'ACTIVE', NOW(), NOW());
SET @user_id_livia = LAST_INSERT_ID();

INSERT INTO customers (customer_type, user_id, phone, street, number, complement, neighborhood, city, state, zip_code, country_code, name, registration_number, birth_date)
VALUES ('PERSONAL', @user_id_livia, '62999990008', 'Rua do Sol', '123', '', 'Setor Bueno', 'Goiânia', 'GO', '74230100', 'BR', 'Lívia Ramos', '88888888888', '1994-09-15');
SET @customer_id_livia = LAST_INSERT_ID();

INSERT INTO accounts (customer_id, agency, number, balance, account_type, account_status, created_at, updated_at, manager_id)
VALUES (@customer_id_livia, '0001', '20008-1', 9200.00, 'CHECKING_ACCOUNT', 'ACTIVE', NOW(), NOW(), @employee_id_sofia);
SET @account_id_livia = LAST_INSERT_ID();

INSERT INTO cards (account_id, number, holder_name, expiration_date, status)
VALUES (@account_id_livia, '4901222233334444', 'LIVIA RAMOS', '2027-12', 'ACTIVE');
SET @card_id_livia = LAST_INSERT_ID();

INSERT INTO card_credit_functions (card_id, credit_limit, available_limit, invoice_closing_day)
VALUES (@card_id_livia, 6000.00, 6000.00, 12);
SET @credit_function_id_livia = @card_id_livia;

INSERT INTO invoices (credit_function_id, total_amount, due_date, closing_date, reference_month, status)
VALUES (@credit_function_id_livia, 350.00, '2025-07-10', '2025-06-30', '2025-06', 'PAID');
SET @invoice_id_livia = LAST_INSERT_ID();

INSERT INTO invoice_items (invoice_id, description, amount, purchase_date)
VALUES (@invoice_id_livia, 'Restaurante Japonês', 120.00, NOW() - INTERVAL 15 DAY);
INSERT INTO invoice_items (invoice_id, description, amount, purchase_date)
VALUES (@invoice_id_livia, 'Loja de Roupas', 230.00, NOW() - INTERVAL 10 DAY);



INSERT INTO users (email, password, role, user_status, created_at, updated_at)
VALUES ('contato@tecbrasil.com', @hashed_password, 'CUSTOMER', 'ACTIVE', NOW(), NOW());
SET @user_id_tecbrasil = LAST_INSERT_ID();

INSERT INTO customers (customer_type, user_id, phone, street, number, complement, neighborhood, city, state, zip_code, country_code, company_name, trade_name, registration_number)
VALUES ('BUSINESS', @user_id_tecbrasil, '21999990009', 'Rua do Comércio', '400', 'Sala 12', 'Centro', 'Niterói', 'RJ', '24020000', 'BR', 'TecBrasil Soluções', 'TecBrasil', '88888888000188');
SET @customer_id_tecbrasil = LAST_INSERT_ID();

INSERT INTO accounts (customer_id, agency, number, balance, account_type, account_status, created_at, updated_at)
VALUES (@customer_id_tecbrasil, '0002', '30003-1', 100000.00, 'CHECKING_ACCOUNT', 'ACTIVE', NOW(), NOW());
SET @account_id_tecbrasil = LAST_INSERT_ID();

INSERT INTO cards (account_id, number, holder_name, expiration_date, status)
VALUES (@account_id_tecbrasil, '5111999977776666', 'TECBRASIL SOLUCOES', '2030-01', 'ACTIVE');
SET @card_id_tecbrasil = LAST_INSERT_ID();

INSERT INTO card_credit_functions (card_id, credit_limit, available_limit, invoice_closing_day)
VALUES (@card_id_tecbrasil, 20000.00, 20000.00, 20);


INSERT INTO users (email, password, role, user_status, created_at, updated_at)
VALUES ('gustavo.menezes@email.com', @hashed_password, 'CUSTOMER', 'ACTIVE', NOW(), NOW());
SET @user_id_gustavo = LAST_INSERT_ID();

INSERT INTO customers (customer_type, user_id, phone, street, number, complement, neighborhood, city, state, zip_code, country_code, name, registration_number, birth_date)
VALUES ('PERSONAL', @user_id_gustavo, '71999990010', 'Av. Brasil', '77', '', 'Pituba', 'Salvador', 'BA', '41830000', 'BR', 'Gustavo Menezes', '99999999999', '1982-04-03');
SET @customer_id_gustavo = LAST_INSERT_ID();

INSERT INTO accounts (customer_id, agency, number, balance, account_type, account_status, created_at, updated_at, manager_id)
VALUES (@customer_id_gustavo, '0003', '40002-1', 100.00, 'CHECKING_ACCOUNT', 'BLOCKED', NOW(), NOW(), @employee_id_sofia);

INSERT INTO loans (customer_id, product_id, principal_amount, interest_rate, number_of_installments, status, disbursement_date, created_at, updated_at)
VALUES (@customer_id_gustavo, @loan_product_3, 8000.00, 0.018, 24, 'ACTIVE', NOW(), NOW(), NOW());


INSERT INTO users (email, password, role, user_status, created_at, updated_at)
VALUES ('rafaela.araujo@email.com', @hashed_password, 'CUSTOMER', 'ACTIVE', NOW(), NOW());
SET @user_id_rafaela = LAST_INSERT_ID();

INSERT INTO customers (customer_type, user_id, phone, street, number, complement, neighborhood, city, state, zip_code, country_code, name, registration_number, birth_date)
VALUES ('PERSONAL', @user_id_rafaela, '11999990011', 'Rua das Palmeiras', '45', 'Bloco B', 'Jardins', 'São Paulo', 'SP', '01415001', 'BR', 'Rafaela Araújo', '11122233344', '1993-07-20');
SET @customer_id_rafaela = LAST_INSERT_ID();

INSERT INTO accounts (customer_id, agency, number, balance, account_type, account_status, created_at, updated_at, manager_id)
VALUES (@customer_id_rafaela, '0001', '20011-1', 2750.00, 'CHECKING_ACCOUNT', 'ACTIVE', NOW(), NOW(), @employee_id_fernando);
SET @account_id_rafaela = LAST_INSERT_ID();

INSERT INTO cards (account_id, number, holder_name, expiration_date, status)
VALUES (@account_id_rafaela, '4911666699990000', 'RAFAELA ARAUJO', '2026-09', 'ACTIVE');
SET @card_id_rafaela = LAST_INSERT_ID();

INSERT INTO card_credit_functions (card_id, credit_limit, available_limit, invoice_closing_day)
VALUES (@card_id_rafaela, 4000.00, 2850.50, 10);
SET @credit_function_id_rafaela = @card_id_rafaela;

INSERT INTO invoices (credit_function_id, total_amount, due_date, closing_date, reference_month, status)
VALUES (@credit_function_id_rafaela, 1149.50, '2025-06-10', '2025-06-01', '2025-06', 'OVERDUE');
SET @invoice_id_rafaela = LAST_INSERT_ID();

INSERT INTO invoice_items (invoice_id, description, amount, purchase_date)
VALUES (@invoice_id_rafaela, 'Compra em supermercado', 349.50, NOW() - INTERVAL 20 DAY);
INSERT INTO invoice_items (invoice_id, description, amount, purchase_date)
VALUES (@invoice_id_rafaela, 'Viagem por aplicativo', 800.00, NOW() - INTERVAL 18 DAY);


INSERT INTO users (email, password, role, user_status, created_at, updated_at)
VALUES ('financeiro@maxfashion.com', @hashed_password, 'CUSTOMER', 'ACTIVE', NOW(), NOW());
SET @user_id_maxfashion = LAST_INSERT_ID();

INSERT INTO customers (customer_type, user_id, phone, street, number, complement, neighborhood, city, state, zip_code, country_code, company_name, trade_name, registration_number)
VALUES ('BUSINESS', @user_id_maxfashion, '21999990012', 'Rua do Estilo', '888', 'Loja 1', 'Centro', 'Rio de Janeiro', 'RJ', '20040020', 'BR', 'Max Fashion S.A.', 'MaxFashion', '12345678000199');
SET @customer_id_maxfashion = LAST_INSERT_ID();

INSERT INTO accounts (customer_id, agency, number, balance, account_type, account_status, created_at, updated_at)
VALUES (@customer_id_maxfashion, '0002', '30004-1', 134500.00, 'CHECKING_ACCOUNT', 'ACTIVE', NOW(), NOW());
SET @account_id_maxfashion = LAST_INSERT_ID();

INSERT INTO cards (account_id, number, holder_name, expiration_date, status)
VALUES (@account_id_maxfashion, '5333777799990000', 'MAX FASHION S.A.', '2027-02', 'CANCELED');
SET @card_id_maxfashion = LAST_INSERT_ID();

INSERT INTO card_credit_functions (card_id, credit_limit, available_limit, invoice_closing_day)
VALUES (@card_id_maxfashion, 10000.00, 0.00, 5);


INSERT INTO users (email, password, role, user_status, created_at, updated_at)
VALUES ('marcelo.pires@email.com', @hashed_password, 'CUSTOMER', 'ACTIVE', NOW(), NOW());
SET @user_id_marcelo = LAST_INSERT_ID();

INSERT INTO customers (customer_type, user_id, phone, street, number, complement, neighborhood, city, state, zip_code, country_code, name, registration_number, birth_date)
VALUES ('PERSONAL', @user_id_marcelo, '31999990013', 'Rua Central', '999', 'Ap 401', 'Savassi', 'Belo Horizonte', 'MG', '30140071', 'BR', 'Marcelo Pires', '12312312300', '1980-06-05');
SET @customer_id_marcelo = LAST_INSERT_ID();

INSERT INTO accounts (customer_id, agency, number, balance, account_type, account_status, created_at, updated_at, manager_id)
VALUES (@customer_id_marcelo, '0001', '20013-1', 5000.00, 'CHECKING_ACCOUNT', 'ACTIVE', NOW(), NOW(), @employee_id_fernando);

INSERT INTO loans (customer_id, product_id, principal_amount, interest_rate, number_of_installments, status, disbursement_date, created_at, updated_at)
VALUES (@customer_id_marcelo, @loan_product_1, 10000.00, 0.04, 12, 'PAID_OFF', NOW() - INTERVAL 1 YEAR, NOW() - INTERVAL 1 YEAR, NOW() - INTERVAL 1 YEAR);


-- Analista: Lucas Pereira
INSERT INTO users (email, password, role, user_status, created_at, updated_at)
VALUES ('lucas.pereira@bank.com', @hashed_password, 'ANALYST', 'ACTIVE', NOW(), NOW());
SET @user_id_lucas = LAST_INSERT_ID();

INSERT INTO employees (user_id, first_name, last_name, registration_code, hire_date)
VALUES (@user_id_lucas, 'Lucas', 'Pereira', 'FUNC-ANL-004', NOW());


-- Gerente de contas: Mariana Castro
INSERT INTO users (email, password, role, user_status, created_at, updated_at)
VALUES ('mariana.castro@bank.com', @hashed_password, 'ACCOUNT_MANAGER', 'ACTIVE', NOW(), NOW());
SET @user_id_mariana = LAST_INSERT_ID();

INSERT INTO employees (user_id, first_name, last_name, registration_code, hire_date)
VALUES (@user_id_mariana, 'Mariana', 'Castro', 'FUNC-MNG-005', NOW());
SET @employee_id_mariana = LAST_INSERT_ID();

INSERT INTO account_managers (id) VALUES (@employee_id_mariana);


INSERT INTO users (email, password, role, user_status, created_at, updated_at)
VALUES ('karina.dias@email.com', @hashed_password, 'CUSTOMER', 'ACTIVE', NOW(), NOW());
SET @user_id_karina = LAST_INSERT_ID();

INSERT INTO customers (customer_type, user_id, phone, street, number, complement, neighborhood, city, state, zip_code, country_code, name, registration_number, birth_date)
VALUES ('PERSONAL', @user_id_karina, '85999990014', 'Rua das Acácias', '150', 'Ap 302', 'Aldeota', 'Fortaleza', 'CE', '60110000', 'BR', 'Karina Dias', '14141414141', '1992-04-22');
SET @customer_id_karina = LAST_INSERT_ID();

INSERT INTO accounts (customer_id, agency, number, balance, account_type, account_status, created_at, updated_at, manager_id)
VALUES (@customer_id_karina, '0003', '40003-1', 7200.00, 'CHECKING_ACCOUNT', 'ACTIVE', NOW(), NOW(), @employee_id_mariana);




