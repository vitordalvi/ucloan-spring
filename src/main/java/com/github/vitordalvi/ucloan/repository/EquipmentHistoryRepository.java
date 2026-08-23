package com.github.vitordalvi.ucloan.repository;

import com.github.vitordalvi.ucloan.entities.EquipmentHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentHistoryRepository extends JpaRepository<EquipmentHistory, Long> {
    Page<EquipmentHistory> findAllByEquipmentId(Long equipmentId, Pageable pageable);
}
