package com.github.vitordalvi.ucloan.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserChangePasswordDtoRequest(
        @NotBlank @Size(min = 6) String currentPassword,
        @NotBlank @Size(min = 6) String newPassword,
        @NotBlank @Size(min = 6) String confirmPassword
) {}
