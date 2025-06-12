package com.grupo14.turnos.dto;

import com.grupo14.turnos.modelo.Rol;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClienteDTO(
    Long id,

    @NotNull
    String email,

    @NotNull
    String contrasena,

    @NotNull
    Long numeroCliente,

    @NotNull
    Long dni,

    @NotNull @Size(min = 2)
    String nombre,

    @NotNull @Size(min = 2)
    String apellido,

    Rol rol
) {
    // Constructor auxiliar que establece automáticamente el rol
    public ClienteDTO(Long id, String email, String contrasena, Long numeroCliente,
                      Long dni, String nombre, String apellido) {
        this(id, email, contrasena, numeroCliente, dni, nombre, apellido, Rol.CLIENTE);
    }
}
