package com.github.vitordalvi.ucloan.dto.request;

import jakarta.validation.constraints.Size;

public record UserPatchRequestDto(
        @Size(max = 50) String firstName,
        @Size (max = 50) String lastName
) {}
