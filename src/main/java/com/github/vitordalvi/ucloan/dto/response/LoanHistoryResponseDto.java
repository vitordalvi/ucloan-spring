package com.github.vitordalvi.ucloan.dto.response;

import com.github.vitordalvi.ucloan.dto.view.LoanView;
import com.github.vitordalvi.ucloan.entities.enums.LoanStatus;

import java.time.LocalDateTime;

public record LoanHistoryResponseDto(
        Long id,
        Long loanId,
        Long changedById,
        LoanStatus loanStatus,
        String notes,
        LocalDateTime changedAt
) implements LoanView {}