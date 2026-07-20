package com.github.vitordalvi.ucloan.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateLoanRequestDto(
        @NotNull Long borrowerId,
        @NotNull Long equipmentId,
        LocalDateTime endDate,
        String description
) {}
