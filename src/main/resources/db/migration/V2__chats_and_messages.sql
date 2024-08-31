-- Создание таблицы chat_rooms
CREATE TABLE chats
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Создание таблицы messages
CREATE TABLE messages
(
    id        SERIAL PRIMARY KEY,
    chat_id   BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    content   TEXT   NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chat_id) REFERENCES chats (id),
    FOREIGN KEY (sender_id) REFERENCES users (id)
);

-- Создание таблицы user_chat
CREATE TABLE users_chats
(
    user_id BIGINT NOT NULL,
    chat_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, chat_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (chat_id) REFERENCES chats (id)
);