package com.github.vitordalvi.ucloan.services;

import com.github.vitordalvi.ucloan.dto.request.CreateEquipmentRequestDto;
import com.github.vitordalvi.ucloan.dto.request.PatchEquipmentRequestDto;
import com.github.vitordalvi.ucloan.dto.response.EquipmentResponseDto;
import com.github.vitordalvi.ucloan.entities.Equipment;
import com.github.vitordalvi.ucloan.entities.EquipmentModel;
import com.github.vitordalvi.ucloan.exceptions.ResourceNotFoundException;
import com.github.vitordalvi.ucloan.mapper.EquipmentMapper;
import com.github.vitordalvi.ucloan.repository.EquipmentHistoryRepository;
import com.github.vitordalvi.ucloan.repository.EquipmentModelRepository;
import com.github.vitordalvi.ucloan.repository.EquipmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;
    private final EquipmentModelRepository equipmentModelRepository;
    private final EquipmentHistoryRepository equipmentHistoryRepository;

    public EquipmentService(EquipmentRepository equipmentRepository, EquipmentMapper equipmentMapper,
                            EquipmentModelRepository equipmentModelRepository,
                            EquipmentHistoryRepository equipmentHistoryRepository) {
        this.equipmentRepository = equipmentRepository;
        this.equipmentMapper = equipmentMapper;
        this.equipmentModelRepository = equipmentModelRepository;
        this.equipmentHistoryRepository = equipmentHistoryRepository;
    }

    // retorna o equipamento
    public EquipmentResponseDto findById(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        return equipmentMapper.toDto(equipment);
    }

    /* FindAll em List
    public List<EquipmentResponseDto> findAll() {
        List<Equipment> equipments = equipmentRepository.findAll();

        return equipmentMapper.toDtoList(equipments);
    }
    */

    // Retorno dos equipamentos em paginas para melhor performance
    public Page<EquipmentResponseDto> findAll(Pageable pageable) {
        Page<Equipment> equipments = equipmentRepository.findAll(pageable);

        return equipments.map(equipmentMapper::toDto);
    }

    // Criar equipamento com base no Dto, assim faz a criação do Id automaticamente
    public EquipmentResponseDto create(CreateEquipmentRequestDto dto) {
        // Validação de se o equipmentModelId que foi passado realmente existe no banco
        EquipmentModel equipmentModel = equipmentModelRepository.findById(dto.equipmentModelId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        Equipment equipment = equipmentMapper.toEntity(dto);
        equipment.setEquipmentModel(equipmentModel);

        Equipment entity = equipmentRepository.save(equipment);

        return equipmentMapper.toDto(entity);
    }

    // update (PUT), vai ser removido em breve
    public EquipmentResponseDto update(Long id, CreateEquipmentRequestDto dto) {
        EquipmentModel equipmentModel = equipmentModelRepository.findById(dto.equipmentModelId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        equipment.setEquipmentModel(equipmentModel);
        equipmentMapper.updateEntityFromDto(dto, equipment);
        equipmentRepository.save(equipment);

        return equipmentMapper.toDto(equipment);
    }

    // metodo para atualizar o equipamento parcialmente, de acordo com os campos
    public EquipmentResponseDto patch(Long id, PatchEquipmentRequestDto dto) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        if (dto.equipmentModelId() != null) {
            EquipmentModel equipmentModel = equipmentModelRepository.findById(dto.equipmentModelId())
                    .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

            equipment.setEquipmentModel(equipmentModel);
        }

        equipmentMapper.patchEntityFromDto(dto, equipment);
        equipmentRepository.save(equipment);

        return equipmentMapper.toDto(equipment);
    }

    // metodo para deletar um equipamento da base de dados
    // quando eu fizer as alterações nas classes (por exemplo, "proibir" o delete) completo
    // da entidade no banco, apenas desabilitar, por segurança.. esse metodo sera alterado
    public void delete(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        equipmentRepository.delete(equipment);
    }
}
