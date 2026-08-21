ALTER TABLE tb_equipment_history
DROP CONSTRAINT IF EXISTS fk_equipment_model;

ALTER TABLE tb_equipment_history
ADD CONSTRAINT fk_equipment_model FOREIGN KEY (equipment_model_id) REFERENCES tb_equipment_model(id);