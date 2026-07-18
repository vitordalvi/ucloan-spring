package com.github.vitordalvi.ucloan.services;

import com.github.vitordalvi.ucloan.dto.request.CreateEquipmentRequestDto;
import com.github.vitordalvi.ucloan.dto.request.PatchEquipmentRequestDto;
import com.github.vitordalvi.ucloan.dto.response.EquipmentResponseDto;
import com.github.vitordalvi.ucloan.entities.Equipment;
import com.github.vitordalvi.ucloan.entities.EquipmentHistory;
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

    // Retorna o equipamento específico pelo seu Id
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

    // Retorno de todos os equipamentos em página
    public Page<EquipmentResponseDto> findAll(Pageable pageable) {
        Page<Equipment> equipments = equipmentRepository.findAll(pageable);

        return equipments.map(equipmentMapper::toDto);
    }

    // Cria um equipamento
    public EquipmentResponseDto create(CreateEquipmentRequestDto dto) {
        // Validação de se o equipmentModelId que foi passado realmente existe no banco
        EquipmentModel equipmentModel = equipmentModelRepository.findById(dto.equipmentModelId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        Equipment equipment = equipmentMapper.toEntity(dto); // Cria o equipamento (Vai atribuir os campos)
        equipment.setEquipmentModel(equipmentModel); // Seta o modelo do equipamento

        Equipment entity = equipmentRepository.save(equipment); // Salva o equipamento no banco

        return equipmentMapper.toDto(entity); // Retorna o dto do equipamento criado
    }

    // Atualiza todos os campos de um equipamento específico
    public EquipmentResponseDto update(Long id, CreateEquipmentRequestDto dto) {
        // Procura o modelo de equipamento
        EquipmentModel equipmentModel = equipmentModelRepository.findById(dto.equipmentModelId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        // Procura o equipamento
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        equipment.setEquipmentModel(equipmentModel); // Atualiza o modelo do equipamento
        equipmentMapper.updateEntityFromDto(dto, equipment); // Atualiza o equipamento pelo dto
        equipmentRepository.save(equipment); // Salva o equipamento no banco

        return equipmentMapper.toDto(equipment); // Retorna o dto do equipamento
    }

    // Atualiza campos específicos de um equipamento
    public EquipmentResponseDto patch(Long id, PatchEquipmentRequestDto dto) {
        // Procura o equipamento
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        // Se o equipmentModel foi alterado
        if (dto.equipmentModelId() != null) {
            // Procura o modelo de equipamento no banco
            EquipmentModel equipmentModel = equipmentModelRepository.findById(dto.equipmentModelId())
                    .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

            // Se achar, seta o novo modelo de equipamento
            equipment.setEquipmentModel(equipmentModel);
        }

        // Se o estado físico mudou
        if (dto.physicalStatus() != null && dto.physicalStatus() != equipment.getPhysicalStatus()) {
            // Cria um novo histórico de equipamento e coloca o novo estado físico do equipamento
            EquipmentHistory history = new EquipmentHistory(equipment, dto.physicalStatus(), "Status updated");
            equipmentHistoryRepository.save(history); // Salva o histórico no banco
        }

        equipmentMapper.patchEntityFromDto(dto, equipment); // Atualiza os campos do equipamento pelo dto
        equipmentRepository.save(equipment); // Salva alterações no banco

        return equipmentMapper.toDto(equipment); // Retorna o dto do equipamento
    }

    // Deleta um equipamento específico pelo seu id
    // Esse metodo vai ser refatorado para uma ideia de "soft delete"
    public void delete(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        equipmentRepository.delete(equipment);
    }
}
