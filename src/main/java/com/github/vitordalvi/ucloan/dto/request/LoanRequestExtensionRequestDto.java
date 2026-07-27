package com.github.vitordalvi.ucloan.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record LoanRequestExtensionRequestDto(
        @NotNull Long loanId,
        @NotNull @JsonFormat(pattern = "dd/MM/yyyy") LocalDateTime extendUntil
) {}
