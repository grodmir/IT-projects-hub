CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,

    token VARCHAR(36) NOT NULL UNIQUE,
    username VARCHAR(100) NOT NULL,

    expires_at TIMESTAMP NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_refresh_token_user
    FOREIGN KEY (username)
        REFERENCES users(username)
        ON DELETE CASCADE
);

CREATE INDEX idx_refresh_tokens_username ON refresh_tokens(username);
CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(token);