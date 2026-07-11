CREATE TABLE tb_user (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE tb_equipment_model (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120),
    manufacturer VARCHAR(120)
);

CREATE TABLE tb_equipment (
    id BIGSERIAL PRIMARY KEY,
    description TEXT,
    equipment_model_id BIGINT NOT NULL,
    loan_status VARCHAR(30) NOT NULL,
    physical_status VARCHAR(30) NOT NULL,

    CONSTRAINT fk_equipment_model FOREIGN KEY (equipment_model_id) REFERENCES tb_equipment_model(id)
);

CREATE TABLE tb_equipment_history (
    id BIGSERIAL PRIMARY KEY,
    equipment_id BIGINT NOT NULL,
    physical_status VARCHAR(30) NOT NULL,
    loan_status VARCHAR(30) NOT NULL,
    notes TEXT,
    changed_at TIMESTAMP NOT NULL,
    changed_by_id BIGINT NOT NULL,

    CONSTRAINT fk_changed_by_id FOREIGN KEY (changed_by_id) REFERENCES tb_user(id),
    CONSTRAINT fk_equipment_id FOREIGN KEY (equipment_id) REFERENCES tb_equipment(id)
);

CREATE TABLE tb_loan (
    id BIGSERIAL PRIMARY KEY,
    borrower_id BIGINT NOT NULL,
    loan_status VARCHAR(30) NOT NULL,
    equipment_id BIGINT NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP,
    description TEXT,

    CONSTRAINT fk_borrower_id FOREIGN KEY (borrower_id) REFERENCES tb_user(id),
    CONSTRAINT fk_equipment_id FOREIGN KEY (equipment_id) REFERENCES tb_equipment(id)
);

CREATE TABLE tb_loan_history (
    id BIGSERIAL PRIMARY KEY,
    loan_id BIGINT NOT NULL,
    loan_status VARCHAR(30) NOT NULL,
    changed_at TIMESTAMP NOT NULL,
    changed_by_id BIGINT NOT NULL,
    notes TEXT,

    CONSTRAINT fk_loan_id FOREIGN KEY (loan_id) REFERENCES tb_loan(id),
    CONSTRAINT fk_changed_by_id FOREIGN KEY (changed_by_id) REFERENCES tb_user(id)
);