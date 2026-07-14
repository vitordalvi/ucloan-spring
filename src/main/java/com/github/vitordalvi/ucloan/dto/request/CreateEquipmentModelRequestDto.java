package com.github.vitordalvi.ucloan.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateEquipmentModelRequestDto(
        @NotBlank @Size(min = 3, max = 120) String name,
        @NotBlank @Size(min = 3, max = 120) String manufacturer) {}
