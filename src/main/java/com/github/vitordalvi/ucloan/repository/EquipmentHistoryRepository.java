package com.github.vitordalvi.ucloan.repository;

import com.github.vitordalvi.ucloan.entities.EquipmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentHistoryRepository extends JpaRepository<EquipmentHistory, Long> {
    List<EquipmentHistory> findAllByEquipmentId(Long equipmentId);
}
