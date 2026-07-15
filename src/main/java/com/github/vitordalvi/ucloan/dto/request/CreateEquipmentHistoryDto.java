package com.github.vitordalvi.ucloan.dto.request;

import com.github.vitordalvi.ucloan.entities.Equipment;
import com.github.vitordalvi.ucloan.entities.enums.LoanStatus;
import com.github.vitordalvi.ucloan.entities.enums.PhysicalStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateEquipmentHistoryDto(
        @NotNull Equipment equipment,
        @NotNull PhysicalStatus physicalStatus,
        @NotNull LoanStatus loanStatus,
        @NotBlank String notes,
        @NotNull Long changedById) {}
