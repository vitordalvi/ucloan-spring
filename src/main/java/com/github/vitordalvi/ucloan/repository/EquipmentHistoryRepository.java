package com.github.vitordalvi.ucloan.repository;

import com.github.vitordalvi.ucloan.entities.EquipmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentHistoryRepository extends JpaRepository<EquipmentHistory, Long> {
}
