package com.github.vitordalvi.ucloan.controller;

import com.github.vitordalvi.ucloan.dto.request.CreateEquipmentModelRequestDto;
import com.github.vitordalvi.ucloan.dto.request.PatchEquipmentModelRequestDto;
import com.github.vitordalvi.ucloan.dto.response.EquipmentModelAdminResponseDto;
import com.github.vitordalvi.ucloan.dto.response.EquipmentModelResponseDto;
import com.github.vitordalvi.ucloan.dto.view.EquipmentModelView;
import com.github.vitordalvi.ucloan.entities.ApplicationUser;
import com.github.vitordalvi.ucloan.services.EquipmentModelService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/v1/equipment-models")
public class EquipmentModelController {

    private final EquipmentModelService equipmentModelService;

    public EquipmentModelController(EquipmentModelService equipmentModelService) {
        this.equipmentModelService = equipmentModelService;
    }

    // Endpoint para retornar as informações de um modelo de equipamento específico
    @GetMapping("/{id}")
    public ResponseEntity<EquipmentModelView> getEquipmentModelById(@PathVariable Long id,
                                                                    @AuthenticationPrincipal ApplicationUser user) {
        log.info("User {} is fetching equipment model: {}", user.getId(), id);
        return ResponseEntity.ok(equipmentModelService.findById(id, user));
    }

    // Endpoint para criação de um modelo de equipamento
    @PostMapping
    public ResponseEntity<EquipmentModelResponseDto> create(
            @Valid @RequestBody CreateEquipmentModelRequestDto dto,
            UriComponentsBuilder uriBuilder) {

        // Retornar a url do modelo de equipamento criado no body da resposta
        EquipmentModelResponseDto response = equipmentModelService.create(dto);
        URI location = uriBuilder.path("api/v1/equipment-models/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    // Endpoint para atualizar todos os campos do modelo de equipamento específico
    @PutMapping("/{id}")
    public ResponseEntity<EquipmentModelAdminResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateEquipmentModelRequestDto dto,
            @AuthenticationPrincipal ApplicationUser user) {
        log.info("User {} is updating equipment model: {}", user.getId(), id);

        EquipmentModelAdminResponseDto response = equipmentModelService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    // Endpoint para atualizar campos específicos do modelo de equipamento específico
    @PatchMapping("/{id}")
    public ResponseEntity<EquipmentModelAdminResponseDto> patch(
            @PathVariable Long id,
            @Valid @RequestBody PatchEquipmentModelRequestDto dto,
            @AuthenticationPrincipal ApplicationUser user) {
        log.info("User {} is patching equipment model: {}", user.getId(), id);

        EquipmentModelAdminResponseDto response = equipmentModelService.patch(id, dto);

        return ResponseEntity.ok(response);
    }

    // Endpoint para deletar um modelo de equipamento do banco
    // refazer (aplicar a logica de um "soft delete")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal ApplicationUser user) {
        log.info("User {} is trying to deleting equipment model: {}", user.getId(), id);
        equipmentModelService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<EquipmentModelView>> findAll(@PageableDefault(size = 10) Pageable pageable,
                                                                   @AuthenticationPrincipal ApplicationUser user) {
        log.info("User {} is fetching all equipment models", user.getId());
        Page<EquipmentModelView> response = equipmentModelService.findAll(user, pageable);

        return ResponseEntity.ok(response);
    }
}
