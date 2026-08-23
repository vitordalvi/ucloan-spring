package com.github.vitordalvi.ucloan.services;

import com.github.vitordalvi.ucloan.dto.request.CreateEquipmentModelRequestDto;
import com.github.vitordalvi.ucloan.dto.request.PatchEquipmentModelRequestDto;
import com.github.vitordalvi.ucloan.dto.response.EquipmentModelAdminResponseDto;
import com.github.vitordalvi.ucloan.dto.response.EquipmentModelResponseDto;
import com.github.vitordalvi.ucloan.dto.view.EquipmentModelView;
import com.github.vitordalvi.ucloan.dto.view.EquipmentView;
import com.github.vitordalvi.ucloan.entities.ApplicationUser;
import com.github.vitordalvi.ucloan.entities.EquipmentModel;
import com.github.vitordalvi.ucloan.entities.enums.Role;
import com.github.vitordalvi.ucloan.exceptions.ResourceNotFoundException;
import com.github.vitordalvi.ucloan.mapper.EquipmentModelMapper;
import com.github.vitordalvi.ucloan.repository.EquipmentModelRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentModelService {

    private final EquipmentModelRepository equipmentModelRepository;
    private final EquipmentModelMapper equipmentModelMapper;

    public EquipmentModelService(EquipmentModelRepository equipmentModelRepository,
                                 EquipmentModelMapper equipmentModelMapper) {
        this.equipmentModelRepository = equipmentModelRepository;
        this.equipmentModelMapper = equipmentModelMapper;
    }

    // Retorna um modelo de equipamento pelo seu id
    public EquipmentModelView findById(Long id, ApplicationUser user) {
        EquipmentModel equipmentModel = equipmentModelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        if (user.getRole() == Role.ADMIN) {
            return equipmentModelMapper.toDtoAdmin(equipmentModel);
        }

        return equipmentModelMapper.toDto(equipmentModel);
    }

    // Retorna a lista com todos os modelos de equipamentos paginados
    public Page<EquipmentModelView> findAll(ApplicationUser user, Pageable pageable) {
        Page<EquipmentModel> equipmentModels = equipmentModelRepository.findAll(pageable);

        if (user.getRole() == Role.ADMIN) {
            return equipmentModels.map(equipmentModelMapper::toDtoAdmin);
        }

        return equipmentModels.map(equipmentModelMapper::toDto);
    }

    // Cria um modelo de equipamento
    public EquipmentModelResponseDto create(CreateEquipmentModelRequestDto dto) {
        EquipmentModel equipmentModel = equipmentModelMapper.toEntity(dto); // Cria a entidade
        EquipmentModel entity = equipmentModelRepository.save(equipmentModel); // Salva a entidade

        return equipmentModelMapper.toDto(entity); // Retorna o dto
    }

    // Atualiza todos os campos do modelo de equipamento
    @Transactional
    public EquipmentModelAdminResponseDto update(Long id, CreateEquipmentModelRequestDto dto) {
        EquipmentModel equipmentModel = equipmentModelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        equipmentModelMapper.updateEntityFromDto(dto, equipmentModel);
        equipmentModelRepository.save(equipmentModel);

        return equipmentModelMapper.toDtoAdmin(equipmentModel);
    }

    // Atualiza os campos específicos do modelo de equipamento
    @Transactional
    public EquipmentModelAdminResponseDto patch(Long id, PatchEquipmentModelRequestDto dto) {
        EquipmentModel equipmentModel = equipmentModelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        equipmentModelMapper.patchEntityFromDto(dto, equipmentModel);
        equipmentModelRepository.save(equipmentModel);

        return equipmentModelMapper.toDtoAdmin(equipmentModel);
    }

    // Deleta o modelo de equipamento específico pelo seu id
    @Transactional
    public void delete(Long id) {
        EquipmentModel equipmentModel = equipmentModelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        equipmentModelRepository.delete(equipmentModel);
    }

}
