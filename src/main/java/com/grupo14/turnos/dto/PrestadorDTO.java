package com.grupo14.turnos.dto;

import jakarta.validation.constraints.NotNull;

public record PrestadorDTO(
        Integer id,
        @NotNull String email,
        @NotNull String contrasena,
        @NotNull String razonSocial,
        @NotNull Boolean habilitado
) {}

