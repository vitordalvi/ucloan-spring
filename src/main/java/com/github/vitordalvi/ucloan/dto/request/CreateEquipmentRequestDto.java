package com.github.vitordalvi.ucloan.dto.request;

import com.github.vitordalvi.ucloan.entities.enums.LoanStatus;
import com.github.vitordalvi.ucloan.entities.enums.PhysicalStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateEquipmentRequestDto(
        @Size(max = 500) String description,
        @NotNull Long equipmentModelId,
        @NotNull PhysicalStatus physicalStatus) {}
