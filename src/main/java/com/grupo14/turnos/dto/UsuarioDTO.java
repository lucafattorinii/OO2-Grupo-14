package com.grupo14.turnos.dto;

import com.grupo14.turnos.modelo.Rol; 
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioDTO(
    Long id,

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    String email,

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    String contrasena,

    Rol rol
) {}

