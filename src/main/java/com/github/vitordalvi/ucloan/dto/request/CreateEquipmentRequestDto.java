package com.github.vitordalvi.ucloan.dto.request;

import com.github.vitordalvi.ucloan.entities.enums.LoanStatus;
import com.github.vitordalvi.ucloan.entities.enums.PhysicalStatus;

public record CreateEquipmentRequestDto(String description, Long equipmentModelId, LoanStatus loanStatus,
                                        PhysicalStatus physicalStatus) {}
