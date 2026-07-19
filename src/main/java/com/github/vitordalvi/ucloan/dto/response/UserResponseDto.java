package com.github.vitordalvi.ucloan.dto.response;

import com.github.vitordalvi.ucloan.entities.enums.Role;

public record UserResponseDto(
        String firstName,
        String lastName,
        String email,
        Role role
) {}
