package com.grupo14.turnos.dto;

import com.grupo14.turnos.modelo.Rol;

import jakarta.validation.constraints.NotNull;

public record PrestadorDTO(
		Long id,
		@NotNull String email,
		@NotNull String contrasena,
	    @NotNull String razonSocial,
	    Boolean habilitado,
	    Rol rol
	) {
	    // Constructor adicional
	    public PrestadorDTO(Long id, String email, String contrasena, String razonSocial, Boolean habilitado) {
	        this(id, email, contrasena, razonSocial, habilitado, Rol.PRESTADOR);
	    }
	}

