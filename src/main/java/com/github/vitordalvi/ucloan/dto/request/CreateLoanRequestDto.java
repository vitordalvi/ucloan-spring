package com.github.vitordalvi.ucloan.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CreateLoanRequestDto(
        @NotNull Long borrowerId,
        @NotNull Long equipmentId,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDateTime endDate,
        @Size(max = 300) String description
) {}
