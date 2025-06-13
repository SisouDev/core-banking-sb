-- #####################################################################
-- # SCRIPT DE SEED PARA TRANSAÇÕES
-- #####################################################################

-- =====================================================================
-- == TRANSAÇÕES PARA A CONTA DE ANA SILVA (Conta número '20001-1')
-- =====================================================================

-- 1. Depósito de Salário
INSERT INTO transactions (`type`, amount, description, `timestamp`, account_id)
VALUES ('DEPOSIT', 7500.00, 'Depósito de Salário - Empresa X', NOW() - INTERVAL 15 DAY, (SELECT id FROM accounts WHERE `number` = '20001-1'));

-- 2. Compra no Débito
INSERT INTO transactions (`type`, amount, description, `timestamp`, account_id)
VALUES ('DEBIT_CARD_PURCHASE', -125.50, 'Compra no débito em: Supermercado Mundial', NOW() - INTERVAL 14 DAY, (SELECT id FROM accounts WHERE `number` = '20001-1'));

-- 3. Pagamento de Fatura de Cartão de Crédito
INSERT INTO transactions (`type`, amount, description, `timestamp`, account_id)
VALUES ('CREDIT_CARD_BILL_PAYMENT', -850.00, 'Pagamento Fatura Cartão Final 2222', NOW() - INTERVAL 10 DAY, (SELECT id FROM accounts WHERE `number` = '20001-1'));

-- 4. Transferência Enviada para Bruno Costa
INSERT INTO transactions (`type`, amount, description, `timestamp`, account_id)
VALUES ('TRANSFER_SENT', -300.00, 'Transferência para Bruno Costa', NOW() - INTERVAL 8 DAY, (SELECT id FROM accounts WHERE `number` = '20001-1'));

-- 5. Saque em caixa eletrônico
INSERT INTO transactions (`type`, amount, description, `timestamp`, account_id)
VALUES ('WITHDRAWAL', -200.00, 'Saque ATM 24h', NOW() - INTERVAL 5 DAY, (SELECT id FROM accounts WHERE `number` = '20001-1'));

-- 6. Pagamento de Parcela de Empréstimo
INSERT INTO transactions (`type`, amount, description, `timestamp`, account_id)
VALUES ('LOAN_INSTALLMENT_PAYMENT', -450.80, 'Pagamento Parcela 1/12 Empréstimo Pessoal', NOW() - INTERVAL 2 DAY, (SELECT id FROM accounts WHERE `number` = '20001-1'));


-- =====================================================================
-- == TRANSAÇÕES PARA A CONTA DE BRUNO COSTA (Conta número '20002-1')
-- =====================================================================

-- 1. Depósito Inicial
INSERT INTO transactions (`type`, amount, description, `timestamp`, account_id)
VALUES ('DEPOSIT', 2000.00, 'Depósito inicial', NOW() - INTERVAL 20 DAY, (SELECT id FROM accounts WHERE `number` = '20002-1'));

-- 2. Transferência Recebida de Ana Silva
INSERT INTO transactions (`type`, amount, description, `timestamp`, account_id)
VALUES ('TRANSFER_RECEIVED', 300.00, 'Transferência de Ana Silva', NOW() - INTERVAL 8 DAY, (SELECT id FROM accounts WHERE `number` = '20002-1'));


-- 1. Investimento em Renda Fixa
INSERT INTO transactions (`type`, amount, description, `timestamp`, account_id)
VALUES ('INVESTMENT_PURCHASE', -2000.00, 'Compra CDB Banco XPTO', NOW() - INTERVAL 6 DAY, (SELECT id FROM accounts WHERE `number` = '20004-1'));

-- 2. Transferência enviada para Ana Silva
INSERT INTO transactions (`type`, amount, description, `timestamp`, account_id)
VALUES ('TRANSFER_SENT', -150.00, 'Ajuda mensal para Ana', NOW() - INTERVAL 3 DAY, (SELECT id FROM accounts WHERE `number` = '20004-1'));

-- 1. Pagamento de Parcela de Empréstimo Corporativo
INSERT INTO transactions (`type`, amount, description, `timestamp`, account_id)
VALUES ('LOAN_INSTALLMENT_PAYMENT', -3200.00, 'Parcela 5/36 - Financiamento Veículo', NOW() - INTERVAL 10 DAY, (SELECT id FROM accounts WHERE `number` = '30002-1'));

-- 2. Pagamento de Fatura de Cartão de Crédito
INSERT INTO transactions (`type`, amount, description, `timestamp`, account_id)
VALUES ('CREDIT_CARD_BILL_PAYMENT', -7000.00, 'Fatura Cartão Corporativo', NOW() - INTERVAL 5 DAY, (SELECT id FROM accounts WHERE `number` = '30002-1'));

-- 3. Depósito Recebido (Cliente)
INSERT INTO transactions (`type`, amount, description, `timestamp`, account_id)
VALUES ('DEPOSIT', 42000.00, 'Pagamento Cliente: Porto Navegações', NOW() - INTERVAL 2 DAY, (SELECT id FROM accounts WHERE `number` = '30002-1'));
