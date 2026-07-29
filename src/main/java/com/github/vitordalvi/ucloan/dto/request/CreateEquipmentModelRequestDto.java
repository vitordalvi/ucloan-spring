package com.github.vitordalvi.ucloan.dto.request;

import com.github.vitordalvi.ucloan.dto.view.EquipmentModelView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateEquipmentModelRequestDto(
        @NotBlank(message = "O campo nome não pode ser nulo")
        @Size(min = 3, max = 120, message = "O campo nome deve conter entre 3 à 120 caracteres")
        String name,
        @NotBlank(message = "O campo fabricante não pode ser nulo")
        @Size(min = 3, max = 120, message = "O campo fabricante deve conter entre 3 à 120 caracteres")
        String manufacturer) implements EquipmentModelView {}
