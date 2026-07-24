package com.github.vitordalvi.ucloan.repository;

import com.github.vitordalvi.ucloan.entities.LoanHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanHistoryRepository extends JpaRepository<LoanHistory, Long> {
    Page<LoanHistory> findAllByLoanId(Long id, Pageable pageable);
}
