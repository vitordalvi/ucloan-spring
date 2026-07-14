package com.github.vitordalvi.ucloan.controller;

import com.github.vitordalvi.ucloan.dto.request.CreateEquipmentModelRequestDto;
import com.github.vitordalvi.ucloan.dto.request.PatchEquipmentModelRequestDto;
import com.github.vitordalvi.ucloan.dto.response.EquipmentModelResponseDto;
import com.github.vitordalvi.ucloan.services.EquipmentModelService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentModelResponseDto> getEquipmentModelById(@PathVariable Long id) {
        return ResponseEntity.ok(equipmentModelService.findById(id));
    }

    @PostMapping
    public ResponseEntity<EquipmentModelResponseDto> create(
            @Valid @RequestBody CreateEquipmentModelRequestDto dto,
            UriComponentsBuilder uriBuilder) {

        EquipmentModelResponseDto response = equipmentModelService.create(dto);
        URI location = uriBuilder.path("api/v1/equipment-models/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentModelResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateEquipmentModelRequestDto dto) {

        EquipmentModelResponseDto response = equipmentModelService.update(id, dto);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EquipmentModelResponseDto> patch(
            @PathVariable Long id,
            @Valid @RequestBody PatchEquipmentModelRequestDto dto) {

        EquipmentModelResponseDto response = equipmentModelService.patch(id, dto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        equipmentModelService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<EquipmentModelResponseDto>> findAll(@PageableDefault(size = 10) Pageable pageable) {
        Page<EquipmentModelResponseDto> response = equipmentModelService.findAll(pageable);

        return ResponseEntity.ok(response);
    }
}
