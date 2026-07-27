package com.github.vitordalvi.ucloan.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.vitordalvi.ucloan.entities.enums.LoanStatus;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record LoanPatchRequestDto(
        Long borrowerId,
        Long equipmentId,
        LoanStatus loanStatus,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDateTime endDate,
        @Size(max = 300) String description
) {}
