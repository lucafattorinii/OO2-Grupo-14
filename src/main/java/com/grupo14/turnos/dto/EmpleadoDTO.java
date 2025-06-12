package com.grupo14.turnos.dto;

import com.grupo14.turnos.modelo.Rol;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EmpleadoDTO(
        Long id,
        @NotNull String email,
        @NotNull String contrasena,
        @NotNull @Size(min = 2) String nombre,
        @NotNull @Size(min = 2) String apellido,
        @NotNull Long dni,
        @NotNull Long cuit,
        @NotNull Integer legajo,
        @NotNull String puestoCargo,
        Long servicioId,
        Rol rol
) {
    // Constructor alternativo que autocompleta el rol
    public EmpleadoDTO(Long id, String email, String contrasena, String nombre, String apellido,
                       Long dni, Long cuit, Integer legajo, String puestoCargo, Long servicioId) {
        this(id, email, contrasena, nombre, apellido, dni, cuit, legajo, puestoCargo, servicioId, Rol.EMPLEADO);
    }
}
