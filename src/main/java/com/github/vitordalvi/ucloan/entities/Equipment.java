package com.github.vitordalvi.ucloan.entities;

import com.github.vitordalvi.ucloan.entities.base.AuditableBaseEntity;
import com.github.vitordalvi.ucloan.entities.enums.LoanStatus;
import com.github.vitordalvi.ucloan.entities.enums.PhysicalStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_equipment")
public class Equipment extends AuditableBaseEntity {

    private String description;

    @ManyToOne
    @JoinColumn(name = "equipment_model_id", nullable = false)
    private EquipmentModel equipmentModel;

    @Enumerated(EnumType.STRING)
    @Column(name = "physical_status")
    private PhysicalStatus physicalStatus;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EquipmentModel getEquipmentModel() {
        return equipmentModel;
    }

    public void setEquipmentModel(EquipmentModel equipmentModel) {
        this.equipmentModel = equipmentModel;
    }

    public PhysicalStatus getPhysicalStatus() {
        return physicalStatus;
    }

    public void setPhysicalStatus(PhysicalStatus physicalStatus) {
        this.physicalStatus = physicalStatus;
    }

}
