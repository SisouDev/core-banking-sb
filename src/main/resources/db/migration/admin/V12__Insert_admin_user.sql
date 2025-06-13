-- Creates the initial admin user.
-- Credentials: admin@bank.com / admin123
INSERT INTO users (email, password, role, user_status, created_at, updated_at)
VALUES (
           'admin@bank.com',
           '$2a$08$7T.IMivu.WMfcOAexT802eYHQHOvYI9qlmVwUBPBx4LWhKP1CSbNO', -- Senha para 'admin123'
           'ADMIN',
           'ACTIVE',
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       );