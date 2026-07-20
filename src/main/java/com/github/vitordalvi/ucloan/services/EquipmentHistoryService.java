package com.github.vitordalvi.ucloan.services;

import com.github.vitordalvi.ucloan.dto.response.EquipmentHistoryResponseDto;
import com.github.vitordalvi.ucloan.entities.EquipmentHistory;
import com.github.vitordalvi.ucloan.exceptions.ResourceNotFoundException;
import com.github.vitordalvi.ucloan.mapper.EquipmentHistoryMapper;
import com.github.vitordalvi.ucloan.repository.EquipmentHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentHistoryService {

    private final EquipmentHistoryRepository equipmentHistoryRepository;
    private final EquipmentHistoryMapper mapper;

    public EquipmentHistoryService(EquipmentHistoryRepository equipmentHistoryRepository,
                                   EquipmentHistoryMapper mapper) {
        this.equipmentHistoryRepository = equipmentHistoryRepository;
        this.mapper = mapper;
    }

    // Retorna um histórico específico de um equipamento
    public EquipmentHistory findById(Long id) {
        return equipmentHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
    }

    // Retorna o histórico do equipamento específico
    public List<EquipmentHistoryResponseDto> findAllByEquipmentId(Long id) {
        return mapper.toDtoList(equipmentHistoryRepository.findAllByEquipmentId(id));
    }
}
