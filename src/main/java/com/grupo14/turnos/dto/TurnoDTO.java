package com.grupo14.turnos.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public record TurnoDTO(
    Integer id,
    @NotNull LocalDate fecha,
    @NotNull LocalTime hora,
    @NotNull String estado,
    @NotNull Integer clienteId,
    @NotNull Integer disponibilidadId,
    @NotNull Long servicioId
) {}
