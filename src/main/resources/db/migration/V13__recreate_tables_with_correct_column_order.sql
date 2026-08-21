-- Migration V13: Recria todas as tabelas com colunas na ordem dos atributos das entidades JPA.
-- IMPORTANTE: Esta migration é irreversível e destrutiva. Fazer backup antes de aplicar.

-- Desabilita verificação de FK para poder dropar na ordem correta
SET session_replication_role = 'replica';

-- Dropa todas as tabelas na ordem inversa de dependência
DROP TABLE IF EXISTS tb_loan_history CASCADE;
DROP TABLE IF EXISTS tb_equipment_history CASCADE;
DROP TABLE IF EXISTS tb_loan CASCADE;
DROP TABLE IF EXISTS tb_token CASCADE;
DROP TABLE IF EXISTS tb_equipment CASCADE;
DROP TABLE IF EXISTS tb_equipment_model CASCADE;
DROP TABLE IF EXISTS tb_user CASCADE;

-- Reabilita verificação de FK
SET session_replication_role = 'origin';

-- ============================================================
-- TABELA: tb_user
-- Entidade: ApplicationUser (extends AuditableBaseEntity)
-- Ordem: id → audit fields → campos da entidade
-- ============================================================
CREATE TABLE tb_user (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_by_id BIGINT,
    first_name VARCHAR(40) NOT NULL,
    last_name VARCHAR(80),
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT true,
    account_non_expired BOOLEAN NOT NULL DEFAULT true,
    account_non_locked BOOLEAN NOT NULL DEFAULT true,
    credentials_non_expired BOOLEAN NOT NULL DEFAULT true,

    CONSTRAINT fk_user_created_by FOREIGN KEY (created_by_id) REFERENCES tb_user(id),
    CONSTRAINT fk_user_updated_by FOREIGN KEY (updated_by_id) REFERENCES tb_user(id)
);

-- ============================================================
-- TABELA: tb_equipment_model
-- Entidade: EquipmentModel (extends AuditableBaseEntity)
-- ============================================================
CREATE TABLE tb_equipment_model (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_by_id BIGINT,
    name VARCHAR(120) NOT NULL,
    manufacturer VARCHAR(120) NOT NULL,

    CONSTRAINT fk_equipment_model_created_by FOREIGN KEY (created_by_id) REFERENCES tb_user(id),
    CONSTRAINT fk_equipment_model_updated_by FOREIGN KEY (updated_by_id) REFERENCES tb_user(id)
);

-- ============================================================
-- TABELA: tb_equipment
-- Entidade: Equipment (extends AuditableBaseEntity)
-- ============================================================
CREATE TABLE tb_equipment (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_by_id BIGINT,
    equipment_model_id BIGINT NOT NULL,
    physical_status VARCHAR(30) NOT NULL,
    description TEXT,

    CONSTRAINT fk_equipment_equipment_model FOREIGN KEY (equipment_model_id) REFERENCES tb_equipment_model(id),
    CONSTRAINT fk_equipment_created_by FOREIGN KEY (created_by_id) REFERENCES tb_user(id),
    CONSTRAINT fk_equipment_updated_by FOREIGN KEY (updated_by_id) REFERENCES tb_user(id)
);

-- ============================================================
-- TABELA: tb_token
-- Entidade: Token (extends BaseEntity - sem audit fields)
-- ============================================================
CREATE TABLE tb_token (
    id BIGSERIAL PRIMARY KEY,
    token TEXT NOT NULL UNIQUE,
    token_type VARCHAR(30) NOT NULL,
    revoked BOOLEAN NOT NULL,
    expired BOOLEAN NOT NULL,
    user_id BIGINT NOT NULL,

    CONSTRAINT fk_token_user FOREIGN KEY (user_id) REFERENCES tb_user(id)
);

-- ============================================================
-- TABELA: tb_loan
-- Entidade: Loan (extends AuditableBaseEntity)
-- ============================================================
CREATE TABLE tb_loan (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_by_id BIGINT,
    borrower_id BIGINT NOT NULL,
    loan_status VARCHAR(30) NOT NULL,
    equipment_id BIGINT NOT NULL,
    start_date TIMESTAMP NOT NULL DEFAULT now(),
    end_date DATE,
    description TEXT,

    CONSTRAINT fk_loan_borrower FOREIGN KEY (borrower_id) REFERENCES tb_user(id),
    CONSTRAINT fk_loan_equipment FOREIGN KEY (equipment_id) REFERENCES tb_equipment(id),
    CONSTRAINT fk_loan_created_by FOREIGN KEY (created_by_id) REFERENCES tb_user(id),
    CONSTRAINT fk_loan_updated_by FOREIGN KEY (updated_by_id) REFERENCES tb_user(id)
);

-- ============================================================
-- TABELA: tb_loan_history
-- Entidade: LoanHistory (extends AuditableBaseEntity)
-- ============================================================
CREATE TABLE tb_loan_history (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_by_id BIGINT,
    loan_id BIGINT NOT NULL,
    loan_status VARCHAR(30) NOT NULL,
    changed_at TIMESTAMP NOT NULL DEFAULT now(),
    changed_by_id BIGINT NOT NULL,
    notes TEXT,

    CONSTRAINT fk_loan_history_loan FOREIGN KEY (loan_id) REFERENCES tb_loan(id),
    CONSTRAINT fk_loan_history_changed_by FOREIGN KEY (changed_by_id) REFERENCES tb_user(id),
    CONSTRAINT fk_loan_history_created_by FOREIGN KEY (created_by_id) REFERENCES tb_user(id),
    CONSTRAINT fk_loan_history_updated_by FOREIGN KEY (updated_by_id) REFERENCES tb_user(id)
);

-- ============================================================
-- TABELA: tb_equipment_history
-- Entidade: EquipmentHistory (extends AuditableBaseEntity)
-- ============================================================
CREATE TABLE tb_equipment_history (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_by_id BIGINT,
    equipment_id BIGINT NOT NULL,
    equipment_model_id BIGINT,
    physical_status VARCHAR(30),
    equipment_description TEXT,
    notes TEXT,

    CONSTRAINT fk_equipment_history_equipment FOREIGN KEY (equipment_id) REFERENCES tb_equipment(id),
    CONSTRAINT fk_equipment_history_equipment_model FOREIGN KEY (equipment_model_id) REFERENCES tb_equipment_model(id),
    CONSTRAINT fk_equipment_history_created_by FOREIGN KEY (created_by_id) REFERENCES tb_user(id),
    CONSTRAINT fk_equipment_history_updated_by FOREIGN KEY (updated_by_id) REFERENCES tb_user(id)
);
