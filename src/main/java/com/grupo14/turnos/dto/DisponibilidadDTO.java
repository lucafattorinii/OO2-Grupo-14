package com.grupo14.turnos.dto;

import jakarta.validation.constraints.NotNull;

public record DisponibilidadDTO(
	    Integer id,
	    @NotNull String diaSemana,
	    @NotNull String horaInicio,
	    @NotNull String horaFin,
	    @NotNull Long servicioId,
	    @NotNull Integer empleadoId
	) {}
