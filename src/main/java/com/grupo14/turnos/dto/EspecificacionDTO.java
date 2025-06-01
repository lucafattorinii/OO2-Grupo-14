package com.grupo14.turnos.dto;

import jakarta.validation.constraints.NotNull;

public record EspecificacionDTO(
	    Integer id,
	    @NotNull Long servicioId,
	    @NotNull String rubro,
	    @NotNull String detalles,
	    @NotNull Integer direccionId
	) {}
