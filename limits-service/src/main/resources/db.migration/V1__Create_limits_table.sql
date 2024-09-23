CREATE TABLE limit
(
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT NOT NULL,
    limit_amount INT    NOT NULL
);

CREATE UNIQUE INDEX idx_limit_user_id ON limit (user_id);