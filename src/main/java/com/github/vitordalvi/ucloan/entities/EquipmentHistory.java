package com.github.vitordalvi.ucloan.entities;

import com.github.vitordalvi.ucloan.entities.base.AuditableBaseEntity;
import com.github.vitordalvi.ucloan.entities.enums.PhysicalStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_equipment_history")
public class EquipmentHistory extends AuditableBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_model_id")
    private EquipmentModel equipmentModel;

    @Enumerated(EnumType.STRING)
    @Column(name = "physical_status")
    private PhysicalStatus physicalStatus;

    private String equipmentDescription;

    private String notes;

    public EquipmentHistory() {}

    public EquipmentHistory(Equipment equipment, EquipmentModel equipmentModel, PhysicalStatus physicalStatus,
                            String equipmentDescription, String notes) {
        this.equipment = equipment;
        this.equipmentModel = equipmentModel;
        this.physicalStatus = physicalStatus;
        this.equipmentDescription = equipmentDescription;
        this.notes = notes;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
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

    public String getEquipmentDescription() {
        return equipmentDescription;
    }

    public void setEquipmentDescription(String equipmentDescription) {
        this.equipmentDescription = equipmentDescription;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}

