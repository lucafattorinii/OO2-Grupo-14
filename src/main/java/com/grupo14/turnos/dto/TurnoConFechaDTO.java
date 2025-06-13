package com.grupo14.turnos.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.grupo14.turnos.modelo.EstadoTurno;

public record TurnoConFechaDTO(
	    LocalDate fecha,
	    LocalTime hora,
	    EstadoTurno estado,
	    Long clienteId,
	    Long disponibilidadId,
	    Long servicioId,
	    Long direccionId
	) {}