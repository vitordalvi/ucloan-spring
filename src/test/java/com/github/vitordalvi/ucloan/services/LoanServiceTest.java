package com.github.vitordalvi.ucloan.services;

import com.github.vitordalvi.ucloan.dto.request.CreateLoanRequestDto;
import com.github.vitordalvi.ucloan.dto.response.LoanResponseDto;
import com.github.vitordalvi.ucloan.entities.ApplicationUser;
import com.github.vitordalvi.ucloan.entities.Equipment;
import com.github.vitordalvi.ucloan.entities.Loan;
import com.github.vitordalvi.ucloan.entities.enums.LoanStatus;
import com.github.vitordalvi.ucloan.exceptions.BusinessException;
import com.github.vitordalvi.ucloan.mapper.LoanMapper;
import com.github.vitordalvi.ucloan.repository.ApplicationUserRepository;
import com.github.vitordalvi.ucloan.repository.EquipmentRepository;
import com.github.vitordalvi.ucloan.repository.LoanRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private ApplicationUserRepository applicationUserRepository;

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private LoanMapper loanMapper;

    @InjectMocks
    private LoanService loanService;

    @Test
    @DisplayName("Should create a new loan when provided data is valid")
    void shouldCreateLoanSuccessfully() {
        // Arrange
        CreateLoanRequestDto dto = new CreateLoanRequestDto(
                1L,
                2L,
                LocalDate.now().plusDays(7),
                "Motivo"
        );

        ApplicationUser user = new ApplicationUser();
        Equipment equipment = new Equipment();
        Loan loanEntity = new Loan();
        Loan savedLoan = new Loan();
        LoanResponseDto expectedResponse = new LoanResponseDto(
                1L,
                1L,
                LoanStatus.BORROWED,
                2L,
                LocalDateTime.now(),
                LocalDate.now().plusDays(7),
                "Motivo"
        );

        when(applicationUserRepository.findByIdAndEnabledTrue(1L)).thenReturn(Optional.of(user));
        when(equipmentRepository.findById(2L)).thenReturn(Optional.of(equipment));
        when(loanRepository.existsByEquipmentIdAndLoanStatus(2L, LoanStatus.BORROWED)).thenReturn(false);
        when(loanMapper.toEntity(dto)).thenReturn(loanEntity);
        when(loanRepository.save(loanEntity)).thenReturn(savedLoan);
        when(loanMapper.toDto(savedLoan)).thenReturn(expectedResponse);

        // Act
        LoanResponseDto result = loanService.createLoan(dto);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(loanRepository, times(1)).save(loanEntity);
    }

    @Test
    @DisplayName("Should return exception when equipment is alredy loaned")
    void shouldThrowExceptionWhenEquipmentIsAlredyLoaned() {
        // Arrange
        CreateLoanRequestDto dto = new CreateLoanRequestDto(
                1L,
                2L,
                LocalDate.now().plusDays(3),
                "Motivo"
        );

        when(applicationUserRepository.findByIdAndEnabledTrue(1L)).thenReturn(Optional.of(new ApplicationUser()));
        when(equipmentRepository.findById(2L)).thenReturn(Optional.of(new Equipment()));
        when(loanRepository.existsByEquipmentIdAndLoanStatus(2L, LoanStatus.BORROWED)).thenReturn(true);

        // Act & Assert
        BusinessException ex = assertThrows(BusinessException.class, () ->
                loanService.createLoan(dto));

        assertEquals("Equipment is already loaned!", ex.getMessage());
    }

}
