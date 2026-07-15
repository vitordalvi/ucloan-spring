package com.github.vitordalvi.ucloan.entities;

import com.github.vitordalvi.ucloan.entities.base.AuditableBaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_equipment_model")
public class EquipmentModel extends AuditableBaseEntity {

    private String name;
    private String manufacturer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}
