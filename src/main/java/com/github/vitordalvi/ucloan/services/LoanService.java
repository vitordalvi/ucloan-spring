package com.github.vitordalvi.ucloan.services;

import com.github.vitordalvi.ucloan.dto.request.CreateLoanRequestDto;
import com.github.vitordalvi.ucloan.dto.request.ExtendLoanDurationRequestDto;
import com.github.vitordalvi.ucloan.dto.response.LoanResponseDto;
import com.github.vitordalvi.ucloan.dto.view.LoanView;
import com.github.vitordalvi.ucloan.entities.ApplicationUser;
import com.github.vitordalvi.ucloan.entities.Loan;
import com.github.vitordalvi.ucloan.entities.LoanHistory;
import com.github.vitordalvi.ucloan.entities.enums.LoanStatus;
import com.github.vitordalvi.ucloan.entities.enums.Role;
import com.github.vitordalvi.ucloan.exceptions.BusinessException;
import com.github.vitordalvi.ucloan.exceptions.ResourceNotFoundException;
import com.github.vitordalvi.ucloan.mapper.LoanHistoryMapper;
import com.github.vitordalvi.ucloan.mapper.LoanMapper;
import com.github.vitordalvi.ucloan.repository.ApplicationUserRepository;
import com.github.vitordalvi.ucloan.repository.EquipmentRepository;
import com.github.vitordalvi.ucloan.repository.LoanHistoryRepository;
import com.github.vitordalvi.ucloan.repository.LoanRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;
    private final LoanHistoryRepository loanHistoryRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final EquipmentRepository equipmentRepository;
    private final LoanHistoryMapper loanHistoryMapper;

    public LoanService(LoanRepository loanRepository,
                       LoanMapper loanMapper,
                       LoanHistoryRepository loanHistoryRepository,
                       ApplicationUserRepository applicationUserRepository,
                       EquipmentRepository equipmentRepository, LoanHistoryMapper loanHistoryMapper) {
        this.loanRepository = loanRepository;
        this.loanMapper = loanMapper;
        this.loanHistoryRepository = loanHistoryRepository;
        this.applicationUserRepository = applicationUserRepository;
        this.equipmentRepository = equipmentRepository;
        this.loanHistoryMapper = loanHistoryMapper;
    }

    public LoanView findById(Long id, ApplicationUser requester) throws AccessDeniedException {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found!"));

        if (requester.getRole() == Role.ADMIN) {
            return loanMapper.toAdminDto(loan);
        }

        if (!loan.getBorrower().getId().equals(requester.getId())) {
            throw new AccessDeniedException("You don't have permission to see this loan.");
        }

        return loanMapper.toDto(loan);
    }

    public Page<LoanView> findAllLoans(Pageable pageable) {
        Page<Loan> allLoans = loanRepository.findAll(pageable);

        return allLoans.map(loanMapper::toAdminDto);
    }

    public Page<LoanView> findAllUserLoans(ApplicationUser requester, Pageable pageable) {
        Page<Loan> userLoans = loanRepository.findAllByBorrowerId(requester.getId(), pageable);
        return userLoans.map(loanMapper::toDto);
    }

    public Page<LoanView> findLoanHistoryById(Long id, ApplicationUser requester, Pageable pageable) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found!"));

        if (requester.getRole() != Role.ADMIN && !loan.getBorrower().getId().equals(requester.getId())) {
            throw new AccessDeniedException("You don't have permission to see this loan history.");
        }

        Page<LoanHistory> loanHistory = loanHistoryRepository.findAllByLoanId(id, pageable);

        return loanHistory.map(loanHistoryMapper::toDto);
    }

    public Optional<Long> findBorrowerIdByEquipmentId(Long equipmentId) {
        return loanRepository.findBorrowerIdByEquipmentId(equipmentId);
    }

    @Transactional
    public LoanResponseDto createLoan(CreateLoanRequestDto dto) {
        if (dto.endDate() != null && dto.endDate().isBefore(LocalDate.now())) {
            throw new BusinessException("Loan end date cannot be on the pass!");
        }

        var borrower = applicationUserRepository.findByIdAndEnabledTrue(dto.borrowerId())
                .orElseThrow(() -> new BusinessException("User is disabled or doesn't exists!"));

        var equipment = equipmentRepository.findById(dto.equipmentId())
                .orElseThrow(() -> new BusinessException("Equipment doesn't exists!"));

        boolean isEquipmentLoaned = loanRepository.existsByEquipmentIdAndLoanStatus(dto.equipmentId(), LoanStatus.BORROWED);

        if (isEquipmentLoaned)
            throw new BusinessException("Equipment is already loaned!");

        Loan loan = loanMapper.toEntity(dto);
        loan.setBorrower(borrower);
        loan.setEquipment(equipment);
        loan.setLoanStatus(LoanStatus.BORROWED);
        loan.setEndDate(dto.endDate());
        var savedLoan = loanRepository.save(loan);

        return loanMapper.toDto(savedLoan);
    }

    @Transactional
    public LoanResponseDto extendLoanDuration(Long id,
                                              ExtendLoanDurationRequestDto dto,
                                              ApplicationUser user) {
        log.info("User {} is trying to extend loan {} duration.", user.getId(), id);
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        if (!isEquipmentLoaned(loan.getEquipment().getId())) {
            throw new BusinessException("Cannot extend loan duration. Equipment is not currently loaned.");
        }

        if (!loan.getBorrower().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            log.info("User {} doesn't have permission to extend loan {} duration", user.getId(), id);
            throw new AccessDeniedException("You don't have permission to extend this loan.");
        }

        loan.setEndDate(loan.getEndDate().plusDays(dto.extendDuration().toEpochDay() - loan.getEndDate().toEpochDay()));
        var updatedLoan = loanRepository.save(loan);

        log.info("Loan {} got extended duration to {}", id, dto.extendDuration());
        LoanHistory history = new LoanHistory(loan, user, "Extended loan duration to " + dto.extendDuration());
        loanHistoryRepository.save(history);

        return loanMapper.toDto(updatedLoan);
    }

    public boolean isEquipmentLoaned(Long equipmentId) {
        return loanRepository.existsByEquipmentIdAndLoanStatus(equipmentId, LoanStatus.BORROWED);
    }

}
