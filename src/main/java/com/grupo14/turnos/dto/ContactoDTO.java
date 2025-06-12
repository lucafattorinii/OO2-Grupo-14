package com.grupo14.turnos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ContactoDTO(
    Long id,
    
    @NotBlank
    @Size(max = 20)
    String telefono,

    Long direccionId
) {}