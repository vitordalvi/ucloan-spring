package com.github.vitordalvi.ucloan.controller;

import com.github.vitordalvi.ucloan.dto.response.LoanResponseDto;
import com.github.vitordalvi.ucloan.dto.view.LoanView;
import com.github.vitordalvi.ucloan.entities.ApplicationUser;
import com.github.vitordalvi.ucloan.entities.enums.Role;
import com.github.vitordalvi.ucloan.services.LoanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanView> getLoan(@PathVariable Long id,
                                            Authentication authentication) throws AccessDeniedException {
        var user = (ApplicationUser) authentication.getPrincipal();

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
                                                            Authentication authentication) {
        var user = (ApplicationUser) authentication.getPrincipal();
        Page<LoanView> userLoans = loanService.findAllUserLoans(user, pageable);

        return ResponseEntity.ok(userLoans);
    }
    
    @GetMapping("/{id}/history")
    public ResponseEntity<Page<LoanView>> getLoanHistory(@PathVariable Long id,
                                                         @PageableDefault(size = 10) Pageable pageable,
                                                         Authentication authentication) {
        var user = (ApplicationUser) authentication.getPrincipal();
        Page<LoanView> response = loanService.findLoanHistoryById(id, user, pageable);

        return ResponseEntity.ok(response);
    }
}
