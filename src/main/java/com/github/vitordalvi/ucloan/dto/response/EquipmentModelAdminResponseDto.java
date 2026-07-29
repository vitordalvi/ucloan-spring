package com.github.vitordalvi.ucloan.dto.response;

import com.github.vitordalvi.ucloan.dto.view.EquipmentModelView;

import java.time.LocalDateTime;

public record EquipmentModelAdminResponseDto(Long id,
                                             String name,
                                             String manufacturer,
                                             LocalDateTime createdAt,
                                             LocalDateTime updatedAt,
                                             Long createdById,
                                             Long updatedById) implements EquipmentModelView {}
