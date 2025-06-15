package com.grupo14.turnos.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.grupo14.turnos.modelo.DiaSemana;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.Set;

public record DisponibilidadDTO(
        Long id,
        @NotNull DiaSemana diaSemana,
        @NotNull @JsonFormat(pattern = "HH:mm") LocalTime horaInicio,
        @NotNull @JsonFormat(pattern = "HH:mm") LocalTime horaFin,
        Set<Long> servicioIds // Representa los IDs de los servicios asociados
) {}