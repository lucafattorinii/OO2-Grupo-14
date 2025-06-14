package com.grupo14.turnos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DireccionDTO(
    Long idDireccion,
    
    @NotBlank @Size(max = 50)
    String pais,

    @NotBlank @Size(max = 50)
    String provincia,

    @NotBlank @Size(max = 50)
    String ciudad,

    @Size(max = 100)
    String calle,

    @Size(max = 10)
    String numeroCalle,

    @NotBlank @Size(max = 10)
    String codigoPostal
) {}
