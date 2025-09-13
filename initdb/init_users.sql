CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    fullname VARCHAR(50),
    username VARCHAR(50) NOT NULL UNIQUE CHECK (username ~ '^[a-zA-Z0-9]+$'),
    password_hash CHAR(32) NOT NULL,
    birthdate DATE,
    gender BOOLEAN NOT NULL
);
