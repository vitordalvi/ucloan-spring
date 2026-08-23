package com.github.vitordalvi.ucloan.controller;

import com.github.vitordalvi.ucloan.dto.request.UserAdminPatchRequestDto;
import com.github.vitordalvi.ucloan.dto.request.UserChangePasswordRequestDto;
import com.github.vitordalvi.ucloan.dto.request.UserPatchRequestDto;
import com.github.vitordalvi.ucloan.dto.response.UserAdminResponseDto;
import com.github.vitordalvi.ucloan.dto.response.UserResponseDto;
import com.github.vitordalvi.ucloan.entities.ApplicationUser;
import com.github.vitordalvi.ucloan.mapper.ApplicationUserMapper;
import com.github.vitordalvi.ucloan.services.ApplicationUserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class ApplicationUserController {

    private final ApplicationUserService applicationUserService;

    public ApplicationUserController(ApplicationUserService applicationUserService,
                                     ApplicationUserMapper applicationUserMapper) {
        this.applicationUserService = applicationUserService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMe(@AuthenticationPrincipal ApplicationUser user) {
        log.info("Fetching user information for user with ID: {}", user.getId());
        return ResponseEntity.ok(applicationUserService.getUser(user.getId()));
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponseDto> updateMe(@AuthenticationPrincipal ApplicationUser user,
                                                    @Valid @RequestBody UserPatchRequestDto dto) {

        log.info("Updating user information for user with ID: {}", user.getId());
        return ResponseEntity.ok(applicationUserService.updateUser(user.getId(), dto));
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal ApplicationUser user,
                                               @Valid @RequestBody UserChangePasswordRequestDto dto) {
        log.info("User with ID: {} is changing their password", user.getId());
        applicationUserService.changePassword(user.getId(), dto);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<UserAdminResponseDto> getUserAsAdmin(@PathVariable Long id,
                                                               @AuthenticationPrincipal ApplicationUser user) {
        log.info("Admin with ID: {} is fetching user information for user with ID: {}", user.getId(), id);
        return ResponseEntity.ok(applicationUserService.getUserAsAdmin(id));
    }

    @PatchMapping("/admin/{id}")
    public ResponseEntity<UserAdminResponseDto> updateUserAsAdmin(@PathVariable Long id,
                                                                  @Valid @RequestBody UserAdminPatchRequestDto dto,
                                                                  @AuthenticationPrincipal ApplicationUser user) {
        log.info("Admin with ID: {} is updating user information for user with ID: {}", user.getId(), id);
        return ResponseEntity.ok(applicationUserService.updateUserAsAdmin(id, dto));
    }
}
