package com.github.vitordalvi.ucloan.controller;

import com.github.vitordalvi.ucloan.dto.request.CreateEquipmentRequestDto;
import com.github.vitordalvi.ucloan.dto.request.PatchEquipmentRequestDto;
import com.github.vitordalvi.ucloan.dto.response.EquipmentHistoryResponseDto;
import com.github.vitordalvi.ucloan.dto.response.EquipmentResponseDto;
import com.github.vitordalvi.ucloan.dto.view.EquipmentView;
import com.github.vitordalvi.ucloan.entities.ApplicationUser;
import com.github.vitordalvi.ucloan.services.EquipmentHistoryService;
import com.github.vitordalvi.ucloan.services.EquipmentService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/equipments")
public class EquipmentController {

    private final EquipmentService equipmentService;
    private final EquipmentHistoryService equipmentHistoryService;

    public EquipmentController(EquipmentService equipmentService, EquipmentHistoryService equipmentHistoryService) {
        this.equipmentService = equipmentService;
        this.equipmentHistoryService = equipmentHistoryService;
    }

    @GetMapping
    public ResponseEntity<Page<EquipmentView>> getAllEquipments(@PageableDefault(size = 10) Pageable pageable,
                                                                @AuthenticationPrincipal ApplicationUser user) {
        log.info("User with ID: {} is fetching all equipments available by his profile.", user.getId());
        Page<EquipmentView> response = equipmentService.findAll(user, pageable);
        return ResponseEntity.ok(response);
    }

    // Endpoint para retornar informações do equipmento pelo Id
    @GetMapping("/{id}")
    public ResponseEntity<EquipmentView> getEquipmentById(@PathVariable Long id,
                                                                 @AuthenticationPrincipal ApplicationUser user) {
        log.info("User with ID: {} is fetching equipment with ID: {}.", user.getId(), id);
        return ResponseEntity.ok(equipmentService.findById(id, user));
    }

    // Endpoint para criação de um equipamento
    @Transactional
    @PostMapping
    public ResponseEntity<EquipmentResponseDto> create(
            @Valid @RequestBody CreateEquipmentRequestDto dto,
            UriComponentsBuilder uriBuilder,
            @AuthenticationPrincipal ApplicationUser user) {

        log.info("User with ID: {} is creating a new equipment.", user.getId());
        EquipmentResponseDto response = equipmentService.create(dto, user);

        // Retornar a url do equipamento criado no body da resposta
        URI location = uriBuilder.path("/api/v1/equipments/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    // Endpoint para atualizar todos os campos do equipamento específico
    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<EquipmentResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateEquipmentRequestDto dto,
            @AuthenticationPrincipal ApplicationUser user) {
        log.info("User with ID: {} is updating equipment with ID: {}.", user.getId(), id);
        EquipmentResponseDto response = equipmentService.update(id, dto, user);

        return ResponseEntity.ok(response);
    }

    // Endpoint para atualizar campos específicos do equipamento específico
    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<EquipmentView> patch(
            @PathVariable Long id,
            @Valid @RequestBody PatchEquipmentRequestDto dto,
            @AuthenticationPrincipal ApplicationUser user) {
        log.info("User with ID: {} is patching equipment with ID: {}.", user.getId(), id);
        EquipmentView response = equipmentService.patch(id, dto, user);

        return ResponseEntity.ok(response);
    }

    // Endpoint para deletar um equipamento do banco
    // refazer (aplicar a logica de um "soft delete")
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                      @AuthenticationPrincipal ApplicationUser user) {
        log.info("User with ID: {} is deleting equipment with ID: {}.", user.getId(), id);
        equipmentService.delete(id, user);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para retornar o histórico de um equipamento específico
    // transformar isso em Page (list) não vai ser tão performático caso tenham muitos históricos
    @GetMapping("/{id}/history")
    public ResponseEntity<Page<EquipmentHistoryResponseDto>> getHistory(@PathVariable Long id,
                                                                        @PageableDefault() Pageable pageable) {
        log.info("User is fetching equipment {} history", id);
        Page<EquipmentHistoryResponseDto> history = equipmentHistoryService.findAllByEquipmentId(id, pageable);
        return ResponseEntity.ok(history);
    }

    // Endpoint teste para cargos
    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping("/admin")
    public String testAdminViewing() {
        return "WORKING";
    }
}
