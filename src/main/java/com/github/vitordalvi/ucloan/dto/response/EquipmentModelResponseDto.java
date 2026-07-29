package com.github.vitordalvi.ucloan.dto.response;

import com.github.vitordalvi.ucloan.dto.view.EquipmentModelView;

public record EquipmentModelResponseDto(Long id, String name, String manufacturer) implements EquipmentModelView {}
