ALTER TABLE tb_equipment_history
    ADD COLUMN equipment_model_id BIGINT,
    ADD CONSTRAINT fk_equipment_model FOREIGN KEY (equipment_model_id) REFERENCES tb_equipment_history(id);