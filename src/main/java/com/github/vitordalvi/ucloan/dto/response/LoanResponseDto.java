package com.github.vitordalvi.ucloan.dto.response;

import com.github.vitordalvi.ucloan.dto.view.LoanView;
import com.github.vitordalvi.ucloan.entities.enums.LoanStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LoanResponseDto(
        Long loanId,
        Long borrowerId,
        LoanStatus loanStatus,
        Long equipmentId,
        LocalDateTime startDate,
        LocalDate endDate,
        String description
) implements LoanView {}
