package com.github.vitordalvi.ucloan.mapper;

import com.github.vitordalvi.ucloan.dto.request.CreateEquipmentRequestDto;
import com.github.vitordalvi.ucloan.dto.response.EquipmentResponseDto;
import com.github.vitordalvi.ucloan.entities.Equipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {

    @Mapping(target = "equipmentModel", ignore = true)
    Equipment toEntity(CreateEquipmentRequestDto dto);
    EquipmentResponseDto toDto(Equipment equipment);

    List<EquipmentResponseDto> toDtoList(List<Equipment> equipments);

    void updateEntityFromDto(CreateEquipmentRequestDto dto, @MappingTarget Equipment equipment);
}
