package com.github.vitordalvi.ucloan.services;

import com.github.vitordalvi.ucloan.mapper.LoanHistoryMapper;
import com.github.vitordalvi.ucloan.repository.LoanHistoryRepository;
import com.github.vitordalvi.ucloan.repository.LoanRepository;
import org.springframework.stereotype.Service;

@Service
public class LoanHistoryService {

    private final LoanHistoryRepository loanHistoryRepository;
    private final LoanHistoryMapper loanHistoryMapper;
    private final LoanRepository loanRepository;

    public LoanHistoryService(LoanHistoryRepository loanHistoryRepository,
                              LoanHistoryMapper loanHistoryMapper,
                              LoanRepository loanRepository) {
        this.loanHistoryRepository = loanHistoryRepository;
        this.loanHistoryMapper = loanHistoryMapper;
        this.loanRepository = loanRepository;
    }

}
