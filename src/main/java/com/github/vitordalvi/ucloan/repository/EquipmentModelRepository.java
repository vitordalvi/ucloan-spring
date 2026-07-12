package com.github.vitordalvi.ucloan.repository;

import com.github.vitordalvi.ucloan.entities.EquipmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentModelRepository extends JpaRepository<EquipmentModel, Long> {
}
