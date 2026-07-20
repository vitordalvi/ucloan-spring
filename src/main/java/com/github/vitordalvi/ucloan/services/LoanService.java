package com.github.vitordalvi.ucloan.services;

import com.github.vitordalvi.ucloan.mapper.LoanMapper;
import com.github.vitordalvi.ucloan.repository.LoanHistoryRepository;
import com.github.vitordalvi.ucloan.repository.LoanRepository;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;
    private final LoanHistoryRepository loanHistoryRepository;

    public LoanService(LoanRepository loanRepository,
                       LoanMapper loanMapper,
                       LoanHistoryRepository loanHistoryRepository) {
        this.loanRepository = loanRepository;
        this.loanMapper = loanMapper;
        this.loanHistoryRepository = loanHistoryRepository;
    }


}
