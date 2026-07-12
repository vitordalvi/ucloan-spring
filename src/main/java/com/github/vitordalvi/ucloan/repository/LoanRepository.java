package com.github.vitordalvi.ucloan.repository;

import com.github.vitordalvi.ucloan.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
}
