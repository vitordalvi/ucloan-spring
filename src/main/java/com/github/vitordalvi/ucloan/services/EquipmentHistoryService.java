package com.github.vitordalvi.ucloan.services;

import com.github.vitordalvi.ucloan.entities.EquipmentHistory;
import com.github.vitordalvi.ucloan.exceptions.ResourceNotFoundException;
import com.github.vitordalvi.ucloan.repository.EquipmentHistoryRepository;
import org.springframework.stereotype.Service;

@Service
public class EquipmentHistoryService {

    private final EquipmentHistoryRepository equipmentHistoryRepository;

    public EquipmentHistoryService(EquipmentHistoryRepository equipmentHistoryRepository) {
        this.equipmentHistoryRepository = equipmentHistoryRepository;
    }

    public EquipmentHistory findById(Long id) {
        return equipmentHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
    }

    // Para implementação desse create (EquipmentHistory)
    // vou precisar primeiro implementar o Spring Security, para conseguirmos
    // descobrir o id do autor da ação
    //public EquipmentHistory create()
}
