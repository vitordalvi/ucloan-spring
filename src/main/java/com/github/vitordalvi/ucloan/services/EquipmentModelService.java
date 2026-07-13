package com.github.vitordalvi.ucloan.services;

import com.github.vitordalvi.ucloan.dto.request.CreateEquipmentModelRequestDto;
import com.github.vitordalvi.ucloan.dto.response.EquipmentModelResponseDto;
import com.github.vitordalvi.ucloan.entities.EquipmentModel;
import com.github.vitordalvi.ucloan.exceptions.ResourceNotFoundException;
import com.github.vitordalvi.ucloan.mapper.EquipmentMapper;
import com.github.vitordalvi.ucloan.mapper.EquipmentModelMapper;
import com.github.vitordalvi.ucloan.repository.EquipmentModelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentModelService {

    private final EquipmentModelRepository equipmentModelRepository;
    private final EquipmentModelMapper equipmentModelMapper;

    public EquipmentModelService(EquipmentModelRepository equipmentModelRepository, EquipmentModelMapper equipmentModelMapper, EquipmentMapper equipmentMapper) {
        this.equipmentModelRepository = equipmentModelRepository;
        this.equipmentModelMapper = equipmentModelMapper;
    }

    public EquipmentModelResponseDto findById(Long id) {
        EquipmentModel equipmentModel = equipmentModelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        return equipmentModelMapper.toDto(equipmentModel);
    }

    public List<EquipmentModelResponseDto> findAll() {
        List<EquipmentModel> equipmentModels = equipmentModelRepository.findAll();

        return equipmentModelMapper.toDtoList(equipmentModels);
    }

    public EquipmentModelResponseDto create(CreateEquipmentModelRequestDto dto) {
        EquipmentModel equipmentModel = equipmentModelMapper.toEntity(dto);
        EquipmentModel entity = equipmentModelRepository.save(equipmentModel);

        return equipmentModelMapper.toDto(entity);
    }

    public EquipmentModelResponseDto update(Long id, CreateEquipmentModelRequestDto dto) {
        EquipmentModel equipmentModel = equipmentModelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        equipmentModelMapper.updateEntityFromDto(dto, equipmentModel);
        equipmentModelRepository.save(equipmentModel);

        return equipmentModelMapper.toDto(equipmentModel);
    }

    public void delete(Long id) {
        EquipmentModel equipmentModel = equipmentModelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        equipmentModelRepository.delete(equipmentModel);
    }
}
