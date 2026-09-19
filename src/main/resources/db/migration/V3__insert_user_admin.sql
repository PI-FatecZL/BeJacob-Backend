-- =====================================================
-- INSERT: USER ADMIN
-- Usuário admin gerado para testes e desenvolvimento
-- =====================================================

INSERT INTO users (name, email, password, phone, role)
VALUES (
        'User Teste',
        'user@gmail.com',
        '${ADMIN_PASS}',
        '11909095454',
        'ADMIN'
       );