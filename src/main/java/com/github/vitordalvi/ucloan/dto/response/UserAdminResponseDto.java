package com.github.vitordalvi.ucloan.dto.response;

import com.github.vitordalvi.ucloan.entities.enums.Role;

import java.time.LocalDateTime;

public record UserAdminResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        Role role,
        boolean enabled,
        boolean accountNonExpired,
        boolean accountNonLocked,
        boolean credentialsNonExpired,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long updatedBy,
        Long createdBy
) {}
