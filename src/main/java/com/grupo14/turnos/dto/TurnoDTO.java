package com.grupo14.turnos.dto;

import com.grupo14.turnos.modelo.EstadoTurno;
import com.grupo14.turnos.modelo.Fecha;

import jakarta.validation.constraints.NotNull;


import java.time.LocalTime;

public record TurnoDTO(
    Long id,                      
    @NotNull Long fechaId,     
    @NotNull LocalTime hora,
    @NotNull EstadoTurno estado,   
    @NotNull Long clienteId,      
    @NotNull Long disponibilidadId, 
    @NotNull Long servicioId
) {}
