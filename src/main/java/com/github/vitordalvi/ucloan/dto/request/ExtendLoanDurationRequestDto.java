package com.github.vitordalvi.ucloan.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ExtendLoanDurationRequestDto(
        @NotNull Long loanId,
        @JsonFormat(pattern = "dd/MM/yyyy") @NotNull LocalDate extendDuration
) {}
