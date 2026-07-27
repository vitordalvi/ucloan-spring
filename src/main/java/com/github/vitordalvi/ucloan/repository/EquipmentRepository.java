package com.github.vitordalvi.ucloan.repository;

import com.github.vitordalvi.ucloan.entities.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    @Query("""
        SELECT e from Equipment e
        WHERE e.id NOT IN (
                SELECT l.equipment.id FROM Loan l WHERE l.loanStatus = 'BORROWED'
            )
        """)
    Page<Equipment> findAllAvailable(Pageable pageable);
}
