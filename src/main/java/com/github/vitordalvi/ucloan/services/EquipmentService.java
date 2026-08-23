package com.github.vitordalvi.ucloan.services;

import com.github.vitordalvi.ucloan.dto.request.CreateEquipmentRequestDto;
import com.github.vitordalvi.ucloan.dto.request.PatchEquipmentRequestDto;
import com.github.vitordalvi.ucloan.dto.response.EquipmentResponseDto;
import com.github.vitordalvi.ucloan.dto.view.EquipmentView;
import com.github.vitordalvi.ucloan.entities.ApplicationUser;
import com.github.vitordalvi.ucloan.entities.Equipment;
import com.github.vitordalvi.ucloan.entities.EquipmentHistory;
import com.github.vitordalvi.ucloan.entities.EquipmentModel;
import com.github.vitordalvi.ucloan.entities.enums.Role;
import com.github.vitordalvi.ucloan.exceptions.BusinessException;
import com.github.vitordalvi.ucloan.exceptions.ResourceNotFoundException;
import com.github.vitordalvi.ucloan.mapper.EquipmentMapper;
import com.github.vitordalvi.ucloan.repository.EquipmentHistoryRepository;
import com.github.vitordalvi.ucloan.repository.EquipmentModelRepository;
import com.github.vitordalvi.ucloan.repository.EquipmentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;
    private final EquipmentModelRepository equipmentModelRepository;
    private final EquipmentHistoryRepository equipmentHistoryRepository;
    private final LoanService loanService;

    public EquipmentService(EquipmentRepository equipmentRepository, EquipmentMapper equipmentMapper,
                            EquipmentModelRepository equipmentModelRepository,
                            EquipmentHistoryRepository equipmentHistoryRepository,
                            LoanService loanService) {
        this.equipmentRepository = equipmentRepository;
        this.equipmentMapper = equipmentMapper;
        this.equipmentModelRepository = equipmentModelRepository;
        this.equipmentHistoryRepository = equipmentHistoryRepository;
        this.loanService = loanService;
    }

    // Retorna o equipamento específico pelo seu Id
    public EquipmentView findById(Long id, ApplicationUser user) {
        // Se o equipamento não existir, retorna exception
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        log.info("Equipment with id: {} found", id);
        // Se o usuário for admin
        if (user.getRole() == Role.ADMIN) {
            // Retorna o equipment response de admin
            log.info("Admin user {} is fetching equipment with id: {}", user.getId(), id);
            return equipmentMapper.toDtoAdmin(equipment);
        }

        // Query (Se o equipamento está alugado, trás o usuário que alugou)
        Optional<Long> borrowerId = loanService.findBorrowerIdByEquipmentId(id);

        // Se alugador não for nulo, verifica se o id do alugador é o mesmo do usuário
        if (borrowerId.isPresent() && !borrowerId.get().equals(user.getId())) {
            log.error("User {} is trying to access equipment with id: {}, but is not the borrower", user.getId(), id);
            throw new BusinessException("You don't have access to see this equipment!");
        }

        log.info("User {} is fetching his equipment", user.getId());
        // Retorna o DTO caso passe pelas validações
        return equipmentMapper.toDto(equipment);
    }

    // Retorno de todos os equipamentos em página
    public Page<EquipmentView> findAll(ApplicationUser user, Pageable pageable) {
        // Se o usuário for administrador, retorna todos os equipamentos
        if (user.getRole() == Role.ADMIN) {
            log.info("User {} is fetching all equipments", user.getId());
            Page<Equipment> equipments = equipmentRepository.findAll(pageable);

            return equipments.map(equipmentMapper::toDtoAdmin);
        }

        // Se o cargo for USER, retorna somente os equipamentos que estão disponíveis para empréstimo
        log.info("User {} is fetched all available equipments", user.getId());
        Page<Equipment> equipments = equipmentRepository.findAllAvailable(pageable);

        return equipments.map(equipmentMapper::toDto);
    }

    // Cria um equipamento
    @Transactional
    public EquipmentResponseDto create(CreateEquipmentRequestDto dto, ApplicationUser user) {
        log.info("User {} is creating a new equipment", user.getId());

        // Validação de se o equipmentModelId que foi passado realmente existe no banco
        EquipmentModel equipmentModel = equipmentModelRepository.findById(dto.equipmentModelId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        Equipment equipment = equipmentMapper.toEntity(dto); // Cria o equipamento (Vai atribuir os campos)
        equipment.setEquipmentModel(equipmentModel); // Seta o modelo do equipamento

        Equipment entity = equipmentRepository.save(equipment); // Salva o equipamento no banco

        log.info("Equipment with id: {} created successfully", entity.getId());
        return equipmentMapper.toDto(entity); // Retorna o dto do equipamento criado
    }

    // Atualiza todos os campos de um equipamento específico
    @Transactional
    public EquipmentResponseDto update(Long id, CreateEquipmentRequestDto dto, ApplicationUser user) {
        log.info("User {} is updating equipment with id: {}", user.getId(), id);
        // Procura o modelo de equipamento
        EquipmentModel equipmentModel = equipmentModelRepository.findById(dto.equipmentModelId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        // Procura o equipamento
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        equipment.setEquipmentModel(equipmentModel); // Atualiza o modelo do equipamento
        equipmentMapper.updateEntityFromDto(dto, equipment); // Atualiza o equipamento pelo dto
        equipmentRepository.save(equipment); // Salva o equipamento no banco

        log.info("Equipment with id: {} was updated by user: {}", equipment.getId(), user.getId());
        return equipmentMapper.toDto(equipment); // Retorna o dto do equipamento
    }

    // Atualiza campos específicos de um equipamento
    @Transactional
    public EquipmentView patch(Long id, PatchEquipmentRequestDto dto, ApplicationUser user) {
        log.info("User {} is patching the equipment with ID: {}", user.getId(), id);
        // Procura o equipamento
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        // Se os campos não foram alterados, retorna o equipamento como dto
        if (dto.equipmentModelId() == null && dto.physicalStatus() == null && dto.description() == null) {
            return equipmentMapper.toDtoAdmin(equipment);
        }

        EquipmentHistory history = new EquipmentHistory();

        // Se o equipmentModel foi alterado
        if (dto.equipmentModelId() != null) {
            // Procura o modelo de equipamento no banco
            EquipmentModel equipmentModel = equipmentModelRepository.findById(dto.equipmentModelId())
                    .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

            // Se achar, seta o novo modelo de equipamento
            equipment.setEquipmentModel(equipmentModel);
            history.setEquipmentModel(equipmentModel);

        }

        // Se o estado físico mudou
        if (dto.physicalStatus() != null && dto.physicalStatus() != equipment.getPhysicalStatus()) {
            // Cria um novo histórico de equipamento e coloca o novo estado físico do equipamento
            history.setPhysicalStatus(dto.physicalStatus());
        }

        // Se descrição mudou
        if (dto.description() != null) {
            equipment.setDescription(dto.description()); // Seta a descrição
            history.setEquipmentDescription(dto.description()); // Seta a descrição no histórico
        }

        equipmentMapper.patchEntityFromDto(dto, equipment); // Atualiza os campos do equipamento pelo dto
        equipmentRepository.save(equipment); // Salva alterações no banco

        history.setEquipment(equipment); // Salva o equipamento
        history.setNotes("Equipment updated!"); // Adicionada a nota do update feito
        equipmentHistoryRepository.save(history); // Salva o histórico criado

        log.info("User with id: {} updated the equipment with id: {}", user.getId(), equipment.getId());
        return equipmentMapper.toDtoAdmin(equipment); // Retorna o dto do equipamento
    }

    // Deleta um equipamento específico pelo seu id
    @Transactional
    public void delete(Long id, ApplicationUser user) {
        log.info("User with id: {} is deleting the equipment with id: {}", user.getId(), id);
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        equipmentRepository.delete(equipment);
    }
}
