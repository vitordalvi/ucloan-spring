package com.github.vitordalvi.ucloan.controller;

import com.github.vitordalvi.ucloan.dto.request.CreateEquipmentRequestDto;
import com.github.vitordalvi.ucloan.dto.request.PatchEquipmentRequestDto;
import com.github.vitordalvi.ucloan.dto.response.EquipmentHistoryResponseDto;
import com.github.vitordalvi.ucloan.dto.response.EquipmentResponseDto;
import com.github.vitordalvi.ucloan.services.EquipmentHistoryService;
import com.github.vitordalvi.ucloan.services.EquipmentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/equipments")
public class EquipmentController {

    private final EquipmentService equipmentService;
    private final EquipmentHistoryService equipmentHistoryService;

    public EquipmentController(EquipmentService equipmentService, EquipmentHistoryService equipmentHistoryService) {
        this.equipmentService = equipmentService;
        this.equipmentHistoryService = equipmentHistoryService;
    }

    // Endpoint para retornar informações do equipmento pelo Id
    @GetMapping("/{id}")
    public ResponseEntity<EquipmentResponseDto> getEquipmentById(@PathVariable Long id) {
        return ResponseEntity.ok(equipmentService.findById(id));
    }

    // Endpoint para criação de um equipamento
    @PostMapping
    public ResponseEntity<EquipmentResponseDto> create(
            @Valid @RequestBody CreateEquipmentRequestDto dto,
            UriComponentsBuilder uriBuilder) {

        EquipmentResponseDto response = equipmentService.create(dto);

        // Retornar a url do equipamento criado no body da resposta
        URI location = uriBuilder.path("/api/v1/equipments/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    // Endpoint para atualizar todos os campos do equipamento específico
    @PutMapping("/{id}")
    public ResponseEntity<EquipmentResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateEquipmentRequestDto dto) {

        EquipmentResponseDto response = equipmentService.update(id, dto);

        return ResponseEntity.ok(response);
    }

    // Endpoint para atualizar campos específicos do equipamento específico
    @PatchMapping("/{id}")
    public ResponseEntity<EquipmentResponseDto> patch(
            @PathVariable Long id,
            @Valid @RequestBody PatchEquipmentRequestDto dto) {

        EquipmentResponseDto response = equipmentService.patch(id, dto);

        return ResponseEntity.ok(response);
    }

    // Endpoint para deletar um equipamento do banco
    // refazer (aplicar a logica de um "soft delete")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        equipmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para retornar todos equipamentos em páginas
    @GetMapping
    public ResponseEntity<Page<EquipmentResponseDto>> findAll(@PageableDefault(size = 10) Pageable pageable) {
        Page<EquipmentResponseDto> response = equipmentService.findAll(pageable);

        return ResponseEntity.ok(response);
    }

    // Endpoint para retornar o histórico de um equipamento específico
    // transformar isso em Page (list) não vai ser tão performático caso tenham muitos históricos
    @GetMapping("/{id}/history")
    public ResponseEntity<List<EquipmentHistoryResponseDto>> getHistory(@PathVariable Long id) {
        List<EquipmentHistoryResponseDto> history = equipmentHistoryService.findAllByEquipmentId(id);
        return ResponseEntity.ok(history);
    }

    // Endpoint teste para cargos
    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping("/admin")
    public String testAdminViewing() {
        return "WORKING";
    }
}
