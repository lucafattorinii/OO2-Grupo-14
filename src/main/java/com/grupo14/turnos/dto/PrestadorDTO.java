package com.grupo14.turnos.dto;

import com.grupo14.turnos.modelo.Rol;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PrestadorDTO(
    Long id,

    @NotNull
    @Email
    String email,

    @NotNull
    String contrasena,

    @NotNull
    @Size(min = 2)
    String razonSocial,

    boolean habilitado
    
) {}
