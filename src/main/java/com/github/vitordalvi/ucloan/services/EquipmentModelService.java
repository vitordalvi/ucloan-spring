package com.github.vitordalvi.ucloan.services;

import com.github.vitordalvi.ucloan.dto.request.CreateEquipmentModelRequestDto;
import com.github.vitordalvi.ucloan.dto.request.PatchEquipmentModelRequestDto;
import com.github.vitordalvi.ucloan.dto.response.EquipmentModelResponseDto;
import com.github.vitordalvi.ucloan.entities.EquipmentModel;
import com.github.vitordalvi.ucloan.exceptions.ResourceNotFoundException;
import com.github.vitordalvi.ucloan.mapper.EquipmentMapper;
import com.github.vitordalvi.ucloan.mapper.EquipmentModelMapper;
import com.github.vitordalvi.ucloan.repository.EquipmentModelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // Retorna um modelo de equipamento pelo seu id
    public EquipmentModelResponseDto findById(Long id) {
        EquipmentModel equipmentModel = equipmentModelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        return equipmentModelMapper.toDto(equipmentModel);
    }

    // Retorna a lista com todos os modelos de equipamentos em lista
    public List<EquipmentModelResponseDto> findAll() {
        List<EquipmentModel> equipmentModels = equipmentModelRepository.findAll();

        return equipmentModelMapper.toDtoList(equipmentModels);
    }

    // Cria um modelo de equipamento
    public EquipmentModelResponseDto create(CreateEquipmentModelRequestDto dto) {
        EquipmentModel equipmentModel = equipmentModelMapper.toEntity(dto); // Cria a entidade
        EquipmentModel entity = equipmentModelRepository.save(equipmentModel); // Salva a entidade

        return equipmentModelMapper.toDto(entity); // Retorna o dto
    }

    // Atualiza todos os campos do modelo de equipamento
    public EquipmentModelResponseDto update(Long id, CreateEquipmentModelRequestDto dto) {
        EquipmentModel equipmentModel = equipmentModelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        equipmentModelMapper.updateEntityFromDto(dto, equipmentModel);
        equipmentModelRepository.save(equipmentModel);

        return equipmentModelMapper.toDto(equipmentModel);
    }

    // Atualiza os campos específicos do modelo de equipamento
    public EquipmentModelResponseDto patch(Long id, PatchEquipmentModelRequestDto dto) {
        EquipmentModel equipmentModel = equipmentModelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        equipmentModelMapper.patchEntityFromDto(dto, equipmentModel);
        equipmentModelRepository.save(equipmentModel);

        return equipmentModelMapper.toDto(equipmentModel);
    }

    // Deleta o modelo de equipamento específico pelo seu id
    public void delete(Long id) {
        EquipmentModel equipmentModel = equipmentModelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        equipmentModelRepository.delete(equipmentModel);
    }

    // Retorna todos os modelos de equipamento em forma de página
    public Page<EquipmentModelResponseDto> findAll(Pageable pageable) {
        Page<EquipmentModel> equipmentModels = equipmentModelRepository.findAll(pageable);

        return equipmentModels.map(equipmentModelMapper::toDto);
    }
}
