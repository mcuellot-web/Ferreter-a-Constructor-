package com.ferreteria.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @NotBlank(message = "El nombre es obligatorio") @Size(max = 120) String nombre,
        @NotBlank(message = "El documento es obligatorio") @Size(max = 30) String documento,
        @Size(max = 30) String telefono,
        @Email(message = "Email inválido") @Size(max = 120) String email) {
}
