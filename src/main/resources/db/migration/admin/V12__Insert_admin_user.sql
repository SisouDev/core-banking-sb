INSERT INTO users (email, password, role, user_status, created_at, updated_at)
VALUES (
           'admin@bank.com',
           '$2a$10$w2yM7e2e6b.jZJ.y/i3wE.MPi2f8194a0gGv2UP7xN.C4yA6C5wS.',
           'ADMIN',
           'ACTIVE',
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       );