package com.github.vitordalvi.ucloan.dto.request;

import com.github.vitordalvi.ucloan.entities.enums.Role;
import jakarta.validation.constraints.Size;

public record UserAdminPatchRequestDto(
        @Size(min = 3, max = 90) String firstName,
        @Size(min = 3, max = 90) String lastName,
        Role role,
        boolean enabled,
        boolean accountNonExpired,
        boolean accountNonLocked,
        boolean credentialsNonExpired
) {}
