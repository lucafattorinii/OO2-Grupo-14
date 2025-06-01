package com.grupo14.turnos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EmpleadoDTO(
	    Integer id,
	    @NotNull @Size(min = 2) String nombre,
	    @NotNull @Size(min = 2) String apellido,
	    @NotNull String puestoCargo
	) {}
