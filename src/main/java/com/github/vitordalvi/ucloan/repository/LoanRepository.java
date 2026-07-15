package com.github.vitordalvi.ucloan.repository;

import com.github.vitordalvi.ucloan.entities.Loan;
import com.github.vitordalvi.ucloan.entities.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    boolean existsByEquipmentIdAndLoanStatus(Long equipmentId, LoanStatus loanStatus);
}
