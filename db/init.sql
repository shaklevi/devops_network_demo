CREATE TABLE messages (
    id SERIAL PRIMARY KEY,
    message TEXT NOT NULL
);

INSERT INTO messages (message)
VALUES ('Hello from PostgreSQL running inside Docker!');
