package com.github.vitordalvi.ucloan.entities;

import com.github.vitordalvi.ucloan.entities.base.AuditableBaseEntity;
import com.github.vitordalvi.ucloan.entities.enums.LoanStatus;
import com.github.vitordalvi.ucloan.entities.enums.PhysicalStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_equipment_history")
public class EquipmentHistory extends AuditableBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    @Enumerated(EnumType.STRING)
    @Column(name = "physical_status")
    private PhysicalStatus physicalStatus;

    private String notes;

    @Column(name = "changed_at")
    private LocalDateTime changedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by_id")
    private ApplicationUser changedBy;

    protected EquipmentHistory() {}

    public EquipmentHistory(Equipment equipment, ApplicationUser changedBy, String notes) {
        this.equipment = equipment;
        this.physicalStatus = equipment.getPhysicalStatus();
        this.changedBy = changedBy;
        this.changedAt = LocalDateTime.now();
        this.notes = notes;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public PhysicalStatus getPhysicalStatus() {
        return physicalStatus;
    }

    public void setPhysicalStatus(PhysicalStatus physicalStatus) {
        this.physicalStatus = physicalStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }

    public ApplicationUser getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(ApplicationUser changedBy) {
        this.changedBy = changedBy;
    }
}

