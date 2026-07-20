package com.github.vitordalvi.ucloan.controller;

import com.github.vitordalvi.ucloan.dto.request.UserAdminPatchRequestDto;
import com.github.vitordalvi.ucloan.dto.request.UserChangePasswordDtoRequest;
import com.github.vitordalvi.ucloan.dto.request.UserPatchRequestDto;
import com.github.vitordalvi.ucloan.dto.response.UserAdminResponseDto;
import com.github.vitordalvi.ucloan.dto.response.UserResponseDto;
import com.github.vitordalvi.ucloan.entities.ApplicationUser;
import com.github.vitordalvi.ucloan.mapper.ApplicationUserMapper;
import com.github.vitordalvi.ucloan.services.ApplicationUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
public class ApplicationUserController {

    private final ApplicationUserService applicationUserService;

    public ApplicationUserController(ApplicationUserService applicationUserService,
                                     ApplicationUserMapper applicationUserMapper) {
        this.applicationUserService = applicationUserService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMe(Authentication authentication) {
        var user = (ApplicationUser) authentication.getPrincipal();

        return ResponseEntity.ok(applicationUserService.getUser(user.getId()));
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponseDto> updateMe(Authentication authentication,
                                                    @Valid @RequestBody UserPatchRequestDto dto) {
        var user = (ApplicationUser) authentication.getPrincipal();

        return ResponseEntity.ok(applicationUserService.updateUser(user.getId(), dto));
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(Authentication authentication,
                                               @Valid @RequestBody UserChangePasswordDtoRequest dto) {
        var user = (ApplicationUser) authentication.getPrincipal();
        applicationUserService.changePassword(user.getId(), dto);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<UserAdminResponseDto> getUserAsAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(applicationUserService.getUserAsAdmin(id));
    }

    @PatchMapping("/admin/{id}")
    public ResponseEntity<UserAdminResponseDto> updateUserAsAdmin(@PathVariable Long id,
                                                                  @Valid @RequestBody UserAdminPatchRequestDto dto) {
        return ResponseEntity.ok(applicationUserService.updateUserAsAdmin(id, dto));
    }
}
