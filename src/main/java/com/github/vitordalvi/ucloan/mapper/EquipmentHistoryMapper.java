package com.github.vitordalvi.ucloan.mapper;

import com.github.vitordalvi.ucloan.dto.response.EquipmentHistoryResponseDto;
import com.github.vitordalvi.ucloan.entities.EquipmentHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EquipmentHistoryMapper {

    @Mapping(source = "equipment.id", target = "equipmentId")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "updatedBy.id", target = "updatedById")
    EquipmentHistoryResponseDto toDto(EquipmentHistory equipmentHistory);

    List<EquipmentHistoryResponseDto> toDtoList(List<EquipmentHistory> equipmentHistories);
}