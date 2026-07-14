package com.github.vitordalvi.ucloan.mapper;

import com.github.vitordalvi.ucloan.dto.request.CreateEquipmentModelRequestDto;
import com.github.vitordalvi.ucloan.dto.request.CreateEquipmentRequestDto;
import com.github.vitordalvi.ucloan.dto.request.PatchEquipmentModelRequestDto;
import com.github.vitordalvi.ucloan.dto.response.EquipmentModelResponseDto;
import com.github.vitordalvi.ucloan.entities.EquipmentModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EquipmentModelMapper {

    EquipmentModel toEntity(CreateEquipmentModelRequestDto dto);
    EquipmentModelResponseDto toDto(EquipmentModel equipmentModel);

    List<EquipmentModelResponseDto> toDtoList(List<EquipmentModel> equipmentModels);

    void patchEntityFromDto(PatchEquipmentModelRequestDto dto, @MappingTarget EquipmentModel equipmentModel);
    void updateEntityFromDto(CreateEquipmentModelRequestDto dto, @MappingTarget EquipmentModel equipmentModel);
}
