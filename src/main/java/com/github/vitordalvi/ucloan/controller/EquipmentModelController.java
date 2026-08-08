package com.github.vitordalvi.ucloan.controller;

import com.github.vitordalvi.ucloan.dto.request.CreateEquipmentModelRequestDto;
import com.github.vitordalvi.ucloan.dto.request.PatchEquipmentModelRequestDto;
import com.github.vitordalvi.ucloan.dto.response.EquipmentModelAdminResponseDto;
import com.github.vitordalvi.ucloan.dto.response.EquipmentModelResponseDto;
import com.github.vitordalvi.ucloan.dto.view.EquipmentModelView;
import com.github.vitordalvi.ucloan.dto.view.EquipmentView;
import com.github.vitordalvi.ucloan.entities.ApplicationUser;
import com.github.vitordalvi.ucloan.services.EquipmentModelService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/v1/equipment-models")
public class EquipmentModelController {

    private final EquipmentModelService equipmentModelService;

    public EquipmentModelController(EquipmentModelService equipmentModelService) {
        this.equipmentModelService = equipmentModelService;
    }

    // Endpoint para retornar as informações de um modelo de equipamento específico
    @GetMapping("/{id}")
    public ResponseEntity<EquipmentModelView> getEquipmentModelById(@PathVariable Long id,
                                                                    @AuthenticationPrincipal ApplicationUser user) {

        return ResponseEntity.ok(equipmentModelService.findById(id, user));
    }

    // Endpoint para criação de um modelo de equipamento
    @Transactional
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
    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<EquipmentModelAdminResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateEquipmentModelRequestDto dto) {

        EquipmentModelAdminResponseDto response = equipmentModelService.update(id, dto);

        return ResponseEntity.ok(response);
    }

    // Endpoint para atualizar campos específicos do modelo de equipamento específico
    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<EquipmentModelAdminResponseDto> patch(
            @PathVariable Long id,
            @Valid @RequestBody PatchEquipmentModelRequestDto dto) {

        EquipmentModelAdminResponseDto response = equipmentModelService.patch(id, dto);

        return ResponseEntity.ok(response);
    }

    // Endpoint para deletar um modelo de equipamento do banco
    // refazer (aplicar a logica de um "soft delete")
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        equipmentModelService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<EquipmentModelView>> findAll(@PageableDefault(size = 10) Pageable pageable,
                                                                   @AuthenticationPrincipal ApplicationUser user) {
        Page<EquipmentModelView> response = equipmentModelService.findAll(user, pageable);

        return ResponseEntity.ok(response);
    }
}
