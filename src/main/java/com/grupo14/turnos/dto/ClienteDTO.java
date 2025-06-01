package com.grupo14.turnos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClienteDTO(
	    Integer id,
	    @NotNull String email,
	    @NotNull String contrasena,
	    @NotNull Long numeroCliente,
	    @NotNull Long dni,
	    @NotNull @Size(min = 2) String nombre,
	    @NotNull @Size(min = 2) String apellido
	) {}

