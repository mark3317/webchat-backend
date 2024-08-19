CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE roles
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE user_role
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id)
);

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