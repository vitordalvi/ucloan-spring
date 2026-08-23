package com.github.vitordalvi.ucloan.services;

import com.github.vitordalvi.ucloan.dto.response.EquipmentHistoryResponseDto;
import com.github.vitordalvi.ucloan.entities.EquipmentHistory;
import com.github.vitordalvi.ucloan.exceptions.ResourceNotFoundException;
import com.github.vitordalvi.ucloan.mapper.EquipmentHistoryMapper;
import com.github.vitordalvi.ucloan.repository.EquipmentHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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
        log.info("Trying to find equipment history with ID: {}", id);
        return equipmentHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
    }

    // Retorna o histórico do equipamento específico
    public Page<EquipmentHistoryResponseDto> findAllByEquipmentId(Long id, Pageable pageable) {
        log.info("Trying to find equipment {} all history", id);
        Page<EquipmentHistory> history = equipmentHistoryRepository.findAllByEquipmentId(id, pageable);

        return history.map(mapper::toDto);
    }
}
