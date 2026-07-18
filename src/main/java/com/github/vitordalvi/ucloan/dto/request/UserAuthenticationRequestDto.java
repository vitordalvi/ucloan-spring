package com.github.vitordalvi.ucloan.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserAuthenticationRequestDto(
        @NotBlank @Email String email,
        @NotBlank String password) {}
