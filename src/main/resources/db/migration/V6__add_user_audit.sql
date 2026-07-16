ALTER TABLE tb_user
    DROP COLUMN name;

ALTER TABLE tb_user
    ADD COLUMN first_name VARCHAR(40) NOT NULL,
    ADD COLUMN last_name VARCHAR(80) NOT NULL,
    ADD COLUMN password VARCHAR(255) NOT NULL,
    ADD COLUMN role VARCHAR(50) NOT NULL,
    ADD COLUMN enabled BOOLEAN NOT NULL,
    ADD COLUMN account_non_expired BOOLEAN NOT NULL,
    ADD COLUMN account_non_locked BOOLEAN NOT NULL,
    ADD COLUMN credentials_non_expired BOOLEAN NOT NULL;


