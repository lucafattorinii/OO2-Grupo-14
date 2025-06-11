package com.grupo14.turnos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ServicioDTO(
    Long id,                   
    @NotNull String nombre,
    @NotNull int duracionMin,   
    @Positive int precio,      
    @NotNull Long prestadorId   
) {}
