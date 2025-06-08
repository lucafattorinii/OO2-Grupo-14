package com.grupo14.turnos.dto;

import com.grupo14.turnos.modelo.Rubro;

import jakarta.validation.constraints.NotNull;

public record EspecificacionDTO(
	    Integer id,
	    @NotNull Long servicioId,
	    @NotNull Rubro rubro,
	    @NotNull String detalles,
	    @NotNull Integer direccionId,
	    String servicioNombre
	) {}
