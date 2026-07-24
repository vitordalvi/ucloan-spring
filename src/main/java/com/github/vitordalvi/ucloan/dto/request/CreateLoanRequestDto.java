package com.github.vitordalvi.ucloan.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CreateLoanRequestDto(
        @NotNull Long borrowerId,
        @NotNull Long equipmentId,
        LocalDateTime endDate,
        @Size(max = 300) String description
) {}
