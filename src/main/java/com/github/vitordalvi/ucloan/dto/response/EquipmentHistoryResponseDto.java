package com.github.vitordalvi.ucloan.dto.response;

import com.github.vitordalvi.ucloan.entities.enums.PhysicalStatus;

import java.time.LocalDateTime;

public record EquipmentHistoryResponseDto(
        Long id,
        Long equipmentId,
        PhysicalStatus physicalStatus,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long createdById,
        Long updatedById
) {}
