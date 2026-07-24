package com.github.vitordalvi.ucloan.repository;

import com.github.vitordalvi.ucloan.entities.Loan;
import com.github.vitordalvi.ucloan.entities.enums.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    boolean existsByEquipmentId(Long equipmentId);
    boolean existsByEquipmentIdAndLoanStatus(Long equipmentId, LoanStatus loanStatus);
    Page<Loan> findAllByBorrowerId(Long borrowerId, Pageable pageable);
}
