package com.grupo14.turnos.dto;

public record DireccionDTO(
	    Integer id,
	    String pais,
	    String provincia,
	    String ciudad,
	    String calle,
	    String numeroCalle,
	    String codigoPostal
	) {}
