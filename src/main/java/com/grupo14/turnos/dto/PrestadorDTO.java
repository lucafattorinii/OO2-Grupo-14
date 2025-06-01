package com.grupo14.turnos.dto;

import jakarta.validation.constraints.NotNull;

public record PrestadorDTO(
	    Integer id,
	    @NotNull String razonSocial,
	    @NotNull Boolean habilitado
	) {}
