package com.github.vitordalvi.ucloan.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserRegisterRequestDto(
        @NotBlank String firstName,
        String lastName,
        @NotBlank String email,
        @NotBlank String password) {}
