package com.grupo14.turnos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ServicioDTO(
	    Long idServicio,
	    @NotNull String nombre,
	    @NotNull Integer duracionMin,
	    @Positive Double precio,
	    @NotNull Integer prestadorId
	    
	) {}
