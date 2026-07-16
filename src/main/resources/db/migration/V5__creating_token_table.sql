CREATE TABLE tb_token(
    id BIGSERIAL PRIMARY KEY,
    token TEXT NOT NULL UNIQUE,
    token_type VARCHAR(30) NOT NULL,
    revoked BOOLEAN NOT NULL,
    expired BOOLEAN NOT NULL,
    user_id BIGINT NOT NULL,

    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES tb_user(id)
)

