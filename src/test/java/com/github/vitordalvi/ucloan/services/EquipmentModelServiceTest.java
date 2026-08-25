package com.github.vitordalvi.ucloan.services;

import com.github.vitordalvi.ucloan.dto.request.CreateEquipmentModelRequestDto;
import com.github.vitordalvi.ucloan.dto.request.PatchEquipmentModelRequestDto;
import com.github.vitordalvi.ucloan.dto.response.EquipmentModelAdminResponseDto;
import com.github.vitordalvi.ucloan.dto.response.EquipmentModelResponseDto;
import com.github.vitordalvi.ucloan.dto.view.EquipmentModelView;
import com.github.vitordalvi.ucloan.entities.ApplicationUser;
import com.github.vitordalvi.ucloan.entities.EquipmentModel;
import com.github.vitordalvi.ucloan.entities.enums.Role;
import com.github.vitordalvi.ucloan.exceptions.ResourceNotFoundException;
import com.github.vitordalvi.ucloan.mapper.EquipmentModelMapper;
import com.github.vitordalvi.ucloan.repository.EquipmentModelRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EquipmentModelServiceTest {

    @Mock
    private EquipmentModelRepository equipmentModelRepository;

    @Mock
    private EquipmentModelMapper equipmentModelMapper;

    @InjectMocks
    private EquipmentModelService equipmentModelService;

    @Nested
    @DisplayName("findById Tests")
    class FindByIdTests {

        @Test
        @DisplayName("Should return user DTO when ID exists and requester is USER")
        void shouldReturnEquipmentModelForUserWhenIdExists() {
            // Arrange
            Long id = 1L;
            EquipmentModel entity = new EquipmentModel();
            ApplicationUser user = new ApplicationUser();
            user.setRole(Role.USER);

            EquipmentModelResponseDto expectedDto = new EquipmentModelResponseDto(id, "Dell V50", "Dell");

            when(equipmentModelRepository.findById(id)).thenReturn(Optional.of(entity));
            when(equipmentModelMapper.toDto(entity)).thenReturn(expectedDto);

            // Act
            EquipmentModelView result = equipmentModelService.findById(id, user);

            // Assert
            assertNotNull(result);
            assertEquals(expectedDto, result);
            verify(equipmentModelMapper, never()).toDtoAdmin(any());
        }

        @Test
        @DisplayName("Should return admin DTO when ID exists and requester is ADMIN")
        void shouldReturnEquipmentModelForAdminWhenIdExists() {
            // Arrange
            Long id = 1L;
            EquipmentModel entity = new EquipmentModel();
            ApplicationUser admin = new ApplicationUser();
            admin.setRole(Role.ADMIN);

            EquipmentModelAdminResponseDto expectedDto = new EquipmentModelAdminResponseDto(
                    id, "Dell V50", "Dell", LocalDateTime.now(), LocalDateTime.now(), 1L, 1L);

            when(equipmentModelRepository.findById(id)).thenReturn(Optional.of(entity));
            when(equipmentModelMapper.toDtoAdmin(entity)).thenReturn(expectedDto);

            // Act
            EquipmentModelView result = equipmentModelService.findById(id, admin);

            // Assert
            assertNotNull(result);
            assertEquals(expectedDto, result);
            verify(equipmentModelMapper, never()).toDto(any());
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when ID does not exist")
        void shouldThrowExceptionWhenIdDoesNotExist() {
            // Arrange
            Long id = 99L;
            ApplicationUser user = new ApplicationUser();
            when(equipmentModelRepository.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> equipmentModelService.findById(id, user));
            verifyNoInteractions(equipmentModelMapper);
        }
    }

    @Nested
    @DisplayName("findAll Tests")
    class FindAllTests {

        @Test
        @DisplayName("Should return paged user DTOs when requester is USER")
        void shouldReturnAllPagedForUser() {
            // Arrange
            ApplicationUser user = new ApplicationUser();
            user.setRole(Role.USER);
            Pageable pageable = PageRequest.of(0, 10);

            EquipmentModel entity = new EquipmentModel();
            Page<EquipmentModel> page = new PageImpl<>(List.of(entity));
            EquipmentModelResponseDto dto = new EquipmentModelResponseDto(1L, "Dell V50", "Dell");

            when(equipmentModelRepository.findAll(pageable)).thenReturn(page);
            when(equipmentModelMapper.toDto(entity)).thenReturn(dto);

            // Act
            Page<EquipmentModelView> result = equipmentModelService.findAll(user, pageable);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            assertEquals(dto, result.getContent().get(0));
        }

        @Test
        @DisplayName("Should return paged admin DTOs when requester is ADMIN")
        void shouldReturnAllPagedForAdmin() {
            // Arrange
            ApplicationUser admin = new ApplicationUser();
            admin.setRole(Role.ADMIN);
            Pageable pageable = PageRequest.of(0, 10);

            EquipmentModel entity = new EquipmentModel();
            Page<EquipmentModel> page = new PageImpl<>(List.of(entity));
            EquipmentModelAdminResponseDto adminDto = new EquipmentModelAdminResponseDto(
                    1L, "Dell V50", "Dell", LocalDateTime.now(), LocalDateTime.now(), 1L, 1L);

            when(equipmentModelRepository.findAll(pageable)).thenReturn(page);
            when(equipmentModelMapper.toDtoAdmin(entity)).thenReturn(adminDto);

            // Act
            Page<EquipmentModelView> result = equipmentModelService.findAll(admin, pageable);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            assertEquals(adminDto, result.getContent().get(0));
        }
    }

    @Nested
    @DisplayName("create Tests")
    class CreateTests {

        @Test
        @DisplayName("Should create and return DTO successfully")
        void shouldCreateEquipmentModelSuccessfully() {
            // Arrange
            CreateEquipmentModelRequestDto dto = new CreateEquipmentModelRequestDto("Notebook Dell", "Dell");
            EquipmentModel modelToSave = new EquipmentModel();
            EquipmentModel savedModel = new EquipmentModel();
            EquipmentModelResponseDto expectedDto = new EquipmentModelResponseDto(1L, "Notebook Dell", "Dell");

            when(equipmentModelMapper.toEntity(dto)).thenReturn(modelToSave);
            when(equipmentModelRepository.save(modelToSave)).thenReturn(savedModel);
            when(equipmentModelMapper.toDto(savedModel)).thenReturn(expectedDto);

            // Act
            EquipmentModelResponseDto result = equipmentModelService.create(dto);

            // Assert
            assertNotNull(result);
            assertEquals(expectedDto, result);
            verify(equipmentModelRepository, times(1)).save(modelToSave);
        }
    }

    @Nested
    @DisplayName("update Tests")
    class UpdateTests {

        @Test
        @DisplayName("Should update when ID exists")
        void shouldUpdateWhenIdExists() {
            // Arrange
            Long id = 1L;
            CreateEquipmentModelRequestDto dto = new CreateEquipmentModelRequestDto("Updated Name", "Updated Manufacturer");
            EquipmentModel existingEntity = new EquipmentModel();
            EquipmentModelAdminResponseDto expectedDto = new EquipmentModelAdminResponseDto(
                    id, "Updated Name", "Updated Manufacturer", LocalDateTime.now(), LocalDateTime.now(), 1L, 1L);

            when(equipmentModelRepository.findById(id)).thenReturn(Optional.of(existingEntity));
            when(equipmentModelMapper.toDtoAdmin(existingEntity)).thenReturn(expectedDto);

            // Act
            EquipmentModelAdminResponseDto result = equipmentModelService.update(id, dto);

            // Assert
            assertNotNull(result);
            assertEquals(expectedDto, result);
            verify(equipmentModelMapper).updateEntityFromDto(dto, existingEntity);
            verify(equipmentModelRepository).save(existingEntity);
        }

        @Test
        @DisplayName("Should throw exception when updating non-existent ID")
        void shouldThrowExceptionWhenUpdatingNonExistentId() {
            // Arrange
            Long id = 99L;
            CreateEquipmentModelRequestDto dto = new CreateEquipmentModelRequestDto("Name", "Manufacturer");
            when(equipmentModelRepository.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> equipmentModelService.update(id, dto));
            verify(equipmentModelRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("patch Tests")
    class PatchTests {

        @Test
        @DisplayName("Should patch when ID exists")
        void shouldPatchWhenIdExists() {
            // Arrange
            Long id = 1L;
            PatchEquipmentModelRequestDto dto = new PatchEquipmentModelRequestDto("New Name", null);
            EquipmentModel existingEntity = new EquipmentModel();
            EquipmentModelAdminResponseDto expectedDto = new EquipmentModelAdminResponseDto(
                    id, "New Name", "Old Manufacturer", LocalDateTime.now(), LocalDateTime.now(), 1L, 1L);

            when(equipmentModelRepository.findById(id)).thenReturn(Optional.of(existingEntity));
            when(equipmentModelMapper.toDtoAdmin(existingEntity)).thenReturn(expectedDto);

            // Act
            EquipmentModelAdminResponseDto result = equipmentModelService.patch(id, dto);

            // Assert
            assertNotNull(result);
            assertEquals(expectedDto, result);
            verify(equipmentModelMapper).patchEntityFromDto(dto, existingEntity);
            verify(equipmentModelRepository).save(existingEntity);
        }

        @Test
        @DisplayName("Should throw exception when patching non-existent ID")
        void shouldThrowExceptionWhenPatchingNonExistentId() {
            // Arrange
            Long id = 99L;
            PatchEquipmentModelRequestDto dto = new PatchEquipmentModelRequestDto("New Name", null);
            when(equipmentModelRepository.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> equipmentModelService.patch(id, dto));
            verify(equipmentModelRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("delete Tests")
    class DeleteTests {

        @Test
        @DisplayName("Should delete when ID exists")
        void shouldDeleteWhenIdExists() {
            // Arrange
            Long id = 1L;
            EquipmentModel existingEntity = new EquipmentModel();
            when(equipmentModelRepository.findById(id)).thenReturn(Optional.of(existingEntity));

            // Act
            assertDoesNotThrow(() -> equipmentModelService.delete(id));

            // Assert
            verify(equipmentModelRepository, times(1)).delete(existingEntity);
        }

        @Test
        @DisplayName("Should throw exception and not delete when ID does not exist")
        void shouldThrowExceptionWhenDeletingNonExistentId() {
            // Arrange
            Long id = 99L;
            when(equipmentModelRepository.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> equipmentModelService.delete(id));
            verify(equipmentModelRepository, never()).delete(any());
        }
    }
}
