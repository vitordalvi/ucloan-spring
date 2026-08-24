package com.github.vitordalvi.ucloan.controller;

import com.github.vitordalvi.ucloan.dto.request.CreateLoanRequestDto;
import com.github.vitordalvi.ucloan.dto.request.ExtendLoanDurationRequestDto;
import com.github.vitordalvi.ucloan.dto.response.LoanResponseDto;
import com.github.vitordalvi.ucloan.dto.view.LoanView;
import com.github.vitordalvi.ucloan.entities.ApplicationUser;
import com.github.vitordalvi.ucloan.services.LoanService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanView> getLoan(@PathVariable Long id,
                                            @AuthenticationPrincipal ApplicationUser user) throws AccessDeniedException {
        LoanView response = loanService.findById(id, user);

        return ResponseEntity.ok(response);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<LoanView>> getAllLoans(@PageableDefault(size = 10) Pageable pageable) {
        Page<LoanView> response = loanService.findAllLoans(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-loans")
    public ResponseEntity<Page<LoanView>> getMyLoans(@PageableDefault(size = 10) Pageable pageable,
                                                            @AuthenticationPrincipal ApplicationUser user) {
        Page<LoanView> userLoans = loanService.findAllUserLoans(user, pageable);

        return ResponseEntity.ok(userLoans);
    }
    
    @GetMapping("/{id}/history")
    public ResponseEntity<Page<LoanView>> getLoanHistory(@PathVariable Long id,
                                                         @PageableDefault(size = 10) Pageable pageable,
                                                         @AuthenticationPrincipal ApplicationUser user) {
        Page<LoanView> response = loanService.findLoanHistoryById(id, user, pageable);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LoanResponseDto> createLoan(@Valid @RequestBody CreateLoanRequestDto dto,
                                                      UriComponentsBuilder uriBuilder) {

        LoanResponseDto response = loanService.createLoan(dto);

        URI location = uriBuilder.path("/api/v1/loans/{id}")
                .buildAndExpand(response.loanId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PatchMapping("/{id}/extend-loan")
    public ResponseEntity<LoanResponseDto> extendLoanDuration(@PathVariable Long id,
                                                              @Valid @RequestBody ExtendLoanDurationRequestDto dto,
                                                              @AuthenticationPrincipal ApplicationUser user) {
        log.info("User: {} is trying to extend loan {} duration.", user.getId(), id);
        LoanResponseDto response = loanService.extendLoanDuration(id, dto, user);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/return")
    public ResponseEntity<LoanResponseDto> returnLoan(@PathVariable Long id,
                                           @AuthenticationPrincipal ApplicationUser user) {
        log.info("User {} is trying to return equipment from loan {}", user.getId(), id);
        LoanResponseDto response = loanService.returnLoan(id, user);

        return ResponseEntity.ok(response);
    }

}
