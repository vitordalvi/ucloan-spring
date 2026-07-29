package com.github.vitordalvi.ucloan.services;

import com.github.vitordalvi.ucloan.dto.request.CreateEquipmentModelRequestDto;
import com.github.vitordalvi.ucloan.dto.response.EquipmentModelAdminResponseDto;
import com.github.vitordalvi.ucloan.dto.response.EquipmentModelResponseDto;
import com.github.vitordalvi.ucloan.dto.view.EquipmentModelView;
import com.github.vitordalvi.ucloan.entities.ApplicationUser;
import com.github.vitordalvi.ucloan.entities.EquipmentModel;
import com.github.vitordalvi.ucloan.entities.enums.Role;
import com.github.vitordalvi.ucloan.exceptions.ResourceNotFoundException;
import com.github.vitordalvi.ucloan.mapper.EquipmentModelMapper;
import com.github.vitordalvi.ucloan.repository.EquipmentModelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EquipmentModelServiceTest {

    @Mock
    private EquipmentModelRepository equipmentModelRepository;

    @Mock
    private EquipmentModelMapper equipmentModelMapper;

    @InjectMocks
    private EquipmentModelService equipmentModelService;

    @Test
    void shouldReturnEquipmentModelForUsersWhenIdExists() {
        // Arrange
        Long id = 1L;
        EquipmentModel equipmentModel = new EquipmentModel();
        equipmentModel.setName("Dell V50");

        ApplicationUser user = new ApplicationUser();
        user.setRole(Role.USER);

        EquipmentModelResponseDto expectedDto = new EquipmentModelResponseDto(1L, "Notebok Dell", "Dell");

        when(equipmentModelRepository.findById(id)).thenReturn(Optional.of(equipmentModel));
        when(equipmentModelMapper.toDto(equipmentModel)).thenReturn(expectedDto);

        // Act
        EquipmentModelView result = equipmentModelService.findById(id, user);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDto, result);

    }

    @Test
    void shouldReturnEquipmentModelForAdminsWhenIdExists() {
        // Arrange
        Long id = 1L;
        EquipmentModel equipmentModel = new EquipmentModel();
        equipmentModel.setName("Dell V50");

        ApplicationUser user = new ApplicationUser();
        user.setRole(Role.ADMIN);

        LocalDateTime createdAt = LocalDateTime.of(2026, 4, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 4, 2, 15, 30);

        EquipmentModelAdminResponseDto expectedDto = new EquipmentModelAdminResponseDto(1L,
                "Notebook Dell",
                "Dell",
                createdAt,
                updatedAt,
                2L,
                2L);

        when(equipmentModelRepository.findById(id)).thenReturn(Optional.of(equipmentModel));
        when(equipmentModelMapper.toDtoAdmin(equipmentModel)).thenReturn(expectedDto);

        // Act
        EquipmentModelView result = equipmentModelService.findById(id, user);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDto, result);
    }

    @Test
    void shouldReturnExceptionWhenIdNotExists() {
        // Arrange
        Long id = 1L;

        when(equipmentModelRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> equipmentModelService.findById(id, new ApplicationUser()));
    }

    @Test
    void shouldCreateEquipmentModelSuccessfully() {
        // Arrange
        CreateEquipmentModelRequestDto dto = new CreateEquipmentModelRequestDto(
                "Notebook",
                "Dell"
        );

        EquipmentModel equipmentModelToSave = new EquipmentModel();
        equipmentModelToSave.setName("Notebook Dell");
        equipmentModelToSave.setManufacturer("Dell");

        EquipmentModel savedEquipmentModel = new EquipmentModel();
        savedEquipmentModel.setName("Notebook Dell");
        savedEquipmentModel.setManufacturer("Dell");

        EquipmentModelResponseDto expectedDto = new EquipmentModelResponseDto(
                1L,
                "Notebook Dell",
                "Dell"
        );

        when(equipmentModelMapper.toEntity(dto)).thenReturn(equipmentModelToSave);
        when(equipmentModelRepository.save(equipmentModelToSave)).thenReturn(savedEquipmentModel);
        when(equipmentModelMapper.toDto(savedEquipmentModel)).thenReturn(expectedDto);

        // Act
        EquipmentModelResponseDto result = equipmentModelService.create(dto);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDto, result);
    }
}
