package com.grupo14.turnos.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequestDTO(
    @NotBlank String username,
    @NotBlank String password
) {}