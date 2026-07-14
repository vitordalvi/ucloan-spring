package com.github.vitordalvi.ucloan.dto.response;

import com.github.vitordalvi.ucloan.entities.enums.LoanStatus;
import com.github.vitordalvi.ucloan.entities.enums.PhysicalStatus;

public record EquipmentResponseDto(Long id, String description, LoanStatus loanStatus, PhysicalStatus physicalStatus) {}
