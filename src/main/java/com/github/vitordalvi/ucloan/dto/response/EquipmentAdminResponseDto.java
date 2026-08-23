package com.github.vitordalvi.ucloan.dto.response;

import com.github.vitordalvi.ucloan.dto.view.EquipmentView;
import com.github.vitordalvi.ucloan.entities.enums.PhysicalStatus;

public record EquipmentAdminResponseDto(
        Long equipmentId,
        String description,
        Long equipmentModelId,
        PhysicalStatus physicalStatus
) implements EquipmentView {}