package com.grupo14.turnos.dto;

import com.grupo14.turnos.modelo.DiaSemana;

import java.time.LocalDate;

public record FechaDTO(
    Long id,
    LocalDate fecha,
    DiaSemana diaSemana,
    Long direccionId
) {}