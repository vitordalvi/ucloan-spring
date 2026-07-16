ALTER TABLE tb_equipment
    ADD COLUMN created_by_id BIGINT,
    ADD COLUMN updated_by_id BIGINT,
    ADD CONSTRAINT fk_equipment_created_by FOREIGN KEY (created_by_id) REFERENCES tb_user(id),
    ADD CONSTRAINT fk_equipment_updated_by FOREIGN KEY (updated_by_id) REFERENCES tb_user(id);

ALTER TABLE tb_equipment_model
    ADD COLUMN created_by_id BIGINT,
    ADD COLUMN updated_by_id BIGINT,
    ADD CONSTRAINT fk_equipment_model_created_by FOREIGN KEY (created_by_id) REFERENCES tb_user(id),
    ADD CONSTRAINT fk_equipment_model_updated_by FOREIGN KEY (updated_by_id) REFERENCES tb_user(id);

ALTER TABLE tb_equipment_history
    ADD COLUMN created_by_id BIGINT,
    ADD COLUMN updated_by_id BIGINT,
    ADD CONSTRAINT fk_equipment_history_created_by FOREIGN KEY (created_by_id) REFERENCES tb_user(id),
    ADD CONSTRAINT fk_equipment_history_updated_by FOREIGN KEY (updated_by_id) REFERENCES tb_user(id);

ALTER TABLE tb_user
    ADD COLUMN created_by_id BIGINT,
    ADD COLUMN updated_by_id BIGINT,
    ADD CONSTRAINT fk_user_created_by FOREIGN KEY (created_by_id) REFERENCES tb_user(id),
    ADD CONSTRAINT fk_user_updated_by FOREIGN KEY (updated_by_id) REFERENCES tb_user(id);

ALTER TABLE tb_loan
    ADD COLUMN created_by_id BIGINT,
    ADD COLUMN updated_by_id BIGINT,
    ADD CONSTRAINT fk_loan_created_by FOREIGN KEY (created_by_id) REFERENCES tb_user(id),
    ADD CONSTRAINT fk_loan_updated_by FOREIGN KEY (updated_by_id) REFERENCES tb_user(id);

ALTER TABLE tb_loan_history
    ADD COLUMN created_by_id BIGINT,
    ADD COLUMN updated_by_id BIGINT,
    ADD CONSTRAINT fk_loan_history_created_by FOREIGN KEY (created_by_id) REFERENCES tb_user(id),
    ADD CONSTRAINT fk_loan_history_updated_by FOREIGN KEY (updated_by_id) REFERENCES tb_user(id);