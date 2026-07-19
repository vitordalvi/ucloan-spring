package com.github.vitordalvi.ucloan.dto.request;

import com.github.vitordalvi.ucloan.entities.enums.Role;

public record UserAdminPatchRequestDto(
        String firstName,
        String lastName,
        Role role,
        boolean enabled,
        boolean accountNonExpired,
        boolean accountNonLocked,
        boolean credentialsNonExpired
) {}
