package com.github.vitordalvi.ucloan.dto.request;

import jakarta.validation.constraints.Size;

public record PatchEquipmentModelRequestDto(
        @Size(min = 3, max = 120) String name,
        @Size(min = 3, max = 120) String manufacturer
) {}
