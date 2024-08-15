-- Добавляем роли
INSERT INTO roles (name)
VALUES ('ROLE_USER'),
       ('ROLE_ADMIN');

-- Добавляем админа (пароль admin)
INSERT INTO users (username, password, email)
VALUES ('admin', '$2a$10$9tmvHxdW5D88gbNv.B8WOemwbh8EbNuewuVVtvrA.6o85eo6/IOuu', 'admin@gmail.com');

-- Добавляем админу роли
INSERT INTO user_role (user_id, role_id)
VALUES (1, 1),
       (1, 2);