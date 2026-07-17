package com.github.vitordalvi.ucloan.dto.request;

import com.github.vitordalvi.ucloan.entities.enums.LoanStatus;
import com.github.vitordalvi.ucloan.entities.enums.PhysicalStatus;
import jakarta.validation.constraints.Size;

public record PatchEquipmentRequestDto(
        @Size(max = 500) String description,
        Long equipmentModelId,
        PhysicalStatus physicalStatus
) {}
