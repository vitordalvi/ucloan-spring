package com.github.vitordalvi.ucloan.dto.response;

import com.github.vitordalvi.ucloan.dto.view.LoanView;
import com.github.vitordalvi.ucloan.entities.enums.LoanStatus;

import java.time.LocalDateTime;

public record LoanAdminResponseDto(
        Long borrowerId,
        LoanStatus loanStatus,
        Long equipmentId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long createdBy,
        Long updatedBy
) implements LoanView {}
