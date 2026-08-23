package com.github.vitordalvi.ucloan.dto.request;

import java.time.LocalDate;

public record ExtendLoanDurationRequestDto(
        Long loanId,
        LocalDate extendDuration
) {}
