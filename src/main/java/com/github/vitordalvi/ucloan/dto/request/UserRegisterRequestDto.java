package com.github.vitordalvi.ucloan.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterRequestDto(
        @NotBlank(message = "O primeiro nome é obrigatório")
        @Size(max = 50, message = "O nome não pode exceder 50 caracteres")
        String firstName,

        @Size(max = 50)
        String lastName,

        @NotBlank(message = "A senha é obrigatória")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @NotBlank @Size(min = 6, max = 60, message = "A senha deve ter entre 6 e 60 caracteres.")
        String password) {}
