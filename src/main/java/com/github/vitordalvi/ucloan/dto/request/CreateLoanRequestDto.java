package com.github.vitordalvi.ucloan.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreateLoanRequestDto(
        @NotNull Long borrowerId,
        @NotNull Long equipmentId,

        @JsonFormat(pattern = "dd/MM/yyyy")
        @FutureOrPresent(message = "End date must be today or in the future")
        @NotNull(message = "A data final deve ser inserida no padrão: dd/MM/yyyy") LocalDate endDate,

        @Size(max = 300) String description
) {}
