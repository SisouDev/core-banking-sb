-- #####################################################################
-- # SCRIPT DE SEED PARA PARCELAS DE EMPRÉSTIMO
-- #####################################################################

-- Encontra o ID do empréstimo da Ana Silva (baseado no ID do cliente dela)
SELECT l.id INTO @loan_id_ana FROM loans l
                                       JOIN customers c ON l.customer_id = c.id
                                       JOIN users u ON c.user_id = u.id
WHERE u.email = 'ana.silva@email.com' LIMIT 1;



INSERT INTO loan_installments (loan_id, installment_number, total_amount, principal_amount, interest_amount, due_date, status) VALUES
                                                                                                                                   (@loan_id_ana, 1, 532.76, 332.76, 200.00, '2025-07-13', 'PENDING'),
                                                                                                                                   (@loan_id_ana, 2, 532.76, 346.07, 186.69, '2025-08-13', 'PENDING'),
                                                                                                                                   (@loan_id_ana, 3, 532.76, 359.91, 172.85, '2025-09-13', 'PENDING'),
                                                                                                                                   (@loan_id_ana, 4, 532.76, 374.31, 158.45, '2025-10-13', 'PENDING'),
                                                                                                                                   (@loan_id_ana, 5, 532.76, 389.28, 143.48, '2025-11-13', 'PENDING'),
                                                                                                                                   (@loan_id_ana, 6, 532.76, 404.85, 127.91, '2025-12-13', 'PENDING'),
                                                                                                                                   (@loan_id_ana, 7, 532.76, 421.05, 111.71, '2026-01-13', 'PENDING'),
                                                                                                                                   (@loan_id_ana, 8, 532.76, 437.89, 94.87,  '2026-02-13', 'PENDING'),
                                                                                                                                   (@loan_id_ana, 9, 532.76, 455.40, 77.36,  '2026-03-13', 'PENDING'),
                                                                                                                                   (@loan_id_ana, 10, 532.76, 473.62, 59.14, '2026-04-13', 'PENDING'),
                                                                                                                                   (@loan_id_ana, 11, 532.76, 492.56, 40.20, '2026-05-13', 'PENDING'),
                                                                                                                                   (@loan_id_ana, 12, 532.80, 512.27, 20.53, '2026-06-13', 'PENDING'); -- Pequeno ajuste na última parcela para zerar o saldo